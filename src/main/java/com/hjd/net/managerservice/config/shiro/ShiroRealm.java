package com.hjd.net.managerservice.config.shiro;


import com.hjd.net.managerservice.config.shiro.security.JwtToken;
import com.hjd.net.managerservice.config.shiro.security.JwtUtil;
import com.hjd.net.managerservice.constant.SecurityConsts;
import com.hjd.net.managerservice.entity.sysmgr.Role;
import com.hjd.net.managerservice.entity.sysmgr.User;
import com.hjd.net.managerservice.service.sysmgr.AuthorityService;
import com.hjd.net.managerservice.service.sysmgr.RoleService;
import com.hjd.net.managerservice.service.sysmgr.UserService;
import com.hjd.net.managerservice.utils.JedisUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class ShiroRealm extends AuthorizingRealm {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private JedisUtils jedisUtils;

	@Autowired
	private AuthorityService authorityService;

	@Override
	public boolean supports(AuthenticationToken token) {
		return token instanceof JwtToken;
	}

	/**
	 * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。
	 * @param auth
	 * @return
	 * @throws AuthenticationException
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth)
	        throws AuthenticationException
    {	//获取当前用户的token
		String token = (String)auth.getPrincipal();
        String account  = JwtUtil.getClaim(token, SecurityConsts.ACCOUNT);
        logger.info("==========>用户 {} 调用doGetAuthenticationInfo方法验证权限<==========", account);

		if (account == null)
		{
			throw new AuthenticationException("token invalid");
		}
		if (JwtUtil.verify(token))
		{
            //验证是否失效 从redis获取token
            String key = SecurityConsts.PREFIX_SHIRO_REFRESH_TOKEN + account;
            String rToken = jedisUtils.get(key);
            if(StringUtils.isEmpty(rToken))
            {
				//如果没有  则 token实效  需要重新登陆
                throw new AuthenticationException("Token expired or incorrect.");
            }
            if (!rToken.equals(token))
            {
				//验证redis中的 token 和当前token是否一致  不一致则视为非法访问  需要重新登陆
                throw new AuthenticationException("Access denied, Token verity error.");
            }
		}
        return new SimpleAuthenticationInfo(token, token, "shiroRealm");
    }

	/**
	 * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
	 * @param principals
	 * @return
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals)
    {
        String account = JwtUtil.getClaim(principals.toString(), SecurityConsts.ACCOUNT);
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        User UserInfo = userService.findUserByAccount(account);
        logger.info("==========>用户 {} 调用doGetAuthorizationInfo方法获取权限<==========", account);
        //获取role
		List<Role> RoleList = roleService.findRoleByUserId(UserInfo.getId());
		//获取权限
		List<Object> AuthorityList = authorityService.findByUserId(UserInfo.getId());
		for(Role Role : RoleList)
		{
			authorizationInfo.addRole(Role.getName());
			for(Object auth: AuthorityList)
			{
				authorizationInfo.addStringPermission(auth.toString());
			}
		}
		return authorizationInfo;
	}
}
