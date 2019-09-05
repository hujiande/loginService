package com.hjd.net.managerservice.service.sysmgr.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hjd.net.managerservice.config.shiro.ShiroKit;
import com.hjd.net.managerservice.config.shiro.security.JwtProperties;
import com.hjd.net.managerservice.config.shiro.security.JwtToken;
import com.hjd.net.managerservice.config.shiro.security.JwtUtil;
import com.hjd.net.managerservice.config.shiro.security.UserContext;
import com.hjd.net.managerservice.constant.Constants;
import com.hjd.net.managerservice.constant.SecurityConsts;
import com.hjd.net.managerservice.constant.enumtype.YNFlagStatusEnum;
import com.hjd.net.managerservice.entity.sysmgr.LoginLog;
import com.hjd.net.managerservice.entity.sysmgr.User;
import com.hjd.net.managerservice.entity.sysmgr.UserRole;
import com.hjd.net.managerservice.entity.vo.Result;
import com.hjd.net.managerservice.entity.vo.sysmgr.UserPassword;
import com.hjd.net.managerservice.entity.vo.sysmgr.UserRoleVo;
import com.hjd.net.managerservice.entity.vo.sysmgr.UserVo;
import com.hjd.net.managerservice.mapper.sysmgr.UserMapper;
import com.hjd.net.managerservice.service.sysmgr.LoginLogService;
import com.hjd.net.managerservice.service.sysmgr.UserService;
import com.hjd.net.managerservice.utils.JedisUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author hujiande
 * @since 2018-12-28
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    JwtProperties jwtProperties;

    @Autowired
    JedisUtils jedisUtils;

    @Autowired
    LoginLogService loginLogService;

    @Override
    public User findUserByAccount(String account) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("account", account);
        wrapper.eq("yn_flag", YNFlagStatusEnum.VALID.getCode());
        List<User> userList = baseMapper.selectList(wrapper);
        return userList.size() > 0 ? userList.get(0) : null;
    }

    /**
     * 用户登录
     *
     * @param user
     * @return
     */
    @Override
    public Result login(UserVo user, HttpServletResponse response) {

        Assert.notNull(user.getUsername(), "用户名不能为空");
        Assert.notNull(user.getPassword(), "密码不能为空");

        User userBean = this.findUserByAccount(user.getUsername());
        if (userBean == null) {
            return new Result(false, "用户不存在", null, Constants.PASSWORD_CHECK_INVALID);
        }
        String encodePassword = ShiroKit.md5(user.getPassword(), SecurityConsts.LOGIN_SALT);
        if (!encodePassword.equals(userBean.getPassword())) {
            return new Result(false, "用户名或密码错误", null, Constants.PASSWORD_CHECK_INVALID);
        }
        //账号是否锁定
        if ("0".equals(userBean.getStatus())) {
            return new Result(false, "该账号已被锁定", null, Constants.PASSWORD_CHECK_INVALID);
        }
        String strToken = this.loginSuccess(userBean.getAccount(), response);
        Subject subject = SecurityUtils.getSubject();
        AuthenticationToken token = new JwtToken(strToken);
        subject.login(token);
        //登录成功
        return new Result(true, "登录成功", null, Constants.TOKEN_CHECK_SUCCESS);
    }


    /**
     * 登录后更新缓存，生成token，设置响应头部信息
     *
     * @param account
     * @param response
     */
    private String loginSuccess(String account, HttpServletResponse response) {

        String currentTimeMillis = String.valueOf(System.currentTimeMillis());
        //生成token
        JSONObject json = new JSONObject();
        String token = JwtUtil.sign(account, currentTimeMillis);
        json.put("token", token);
        //token缓存至redis
        String refreshTokenKey = SecurityConsts.PREFIX_SHIRO_REFRESH_TOKEN + account;
        jedisUtils.saveString(refreshTokenKey, token, jwtProperties.getTokenExpireTime());
        //记录登录日志
        LoginLog loginLog = new LoginLog();
        loginLog.setAccount(account);
        loginLog.setLoginTime(Date.from(Instant.now()));
        loginLog.setContent("登录成功");
        loginLog.setYnFlag(YNFlagStatusEnum.VALID.getCode());
        loginLog.setCreator(account);
        loginLog.setEditor(account);
        loginLog.setCreatedTime(loginLog.getLoginTime());
        loginLogService.save(loginLog);
        //写入header
        response.setHeader(SecurityConsts.REQUEST_AUTH_HEADER, token);
        response.setHeader("Access-Control-Expose-Headers", SecurityConsts.REQUEST_AUTH_HEADER);
        return token;
    }

    /**
     * 保存
     *
     * @param user
     * @return
     */
    @Override
    public Result persist(User user) {
        Date currentDate = Date.from(Instant.now());
        if (user.getId() == null) {
            User existUser = this.findUserByAccount(user.getAccount());
            if (existUser != null) {
                //账号已存在
                return new Result(false, "账号已经存在");
            } else {
                //保存密码
                if (!StringUtils.isEmpty(user.getPassword())) {
                    user.setPassword(ShiroKit.md5(user.getPassword(), SecurityConsts.LOGIN_SALT));
                }
                user.setLastPwdModifiedTime(Date.from(Instant.now()));
//                user.setStatus(UserStatusEnum.NORMAL.code());
                user.setYnFlag(YNFlagStatusEnum.VALID.getCode());
                user.setEditor(UserContext.getCurrentUser().getAccount());
                user.setCreator(UserContext.getCurrentUser().getAccount());
                user.setCreatedTime(currentDate);
                user.setModifiedTime(currentDate);
                //新增用户
                baseMapper.insert(user);
            }
        } else {
            User userBean = baseMapper.selectById(user.getId());
            if (user.getAccount().equals(userBean.getAccount())) {
                if (!user.getPassword().equals("******")) {
                    //修改密码
                    user.setPassword(ShiroKit.md5(user.getPassword(), SecurityConsts.LOGIN_SALT));
                    user.setLastPwdModifiedTime(Date.from(Instant.now()));
                } else {
                    user.setPassword(userBean.getPassword());
                    user.setLastPwdModifiedTime(userBean.getLastPwdModifiedTime());
                }
                user.setEditor(UserContext.getCurrentUser().getAccount());
                user.setModifiedTime(currentDate);
                //更新用户
                baseMapper.updateById(user);
            } else {
                return new Result(false, "账号不能修改", null, Constants.PARAMETERS_MISSING);
            }
        }
        return new Result(true, "修改成功", null, Constants.TOKEN_CHECK_SUCCESS);
    }

    @Override
    public Result findUserRole(Long userId) {
        List<Long> auths = baseMapper.selectRoleByUserId(userId);
        return new Result(true, null, auths, Constants.TOKEN_CHECK_SUCCESS);
    }

    /**
     * 保存用户角色
     *
     * @param userRole
     * @return
     */
    @Override
    public Result saveUserRoles(UserRoleVo userRole) {
        Date currentDate = Date.from(Instant.now());
        UserRole user = new UserRole();
        user.setUserId(userRole.getUserId());
        user.setModifiedTime(currentDate);
        baseMapper.deleteRoleByUserId(user);
        UserRole tempUserRole;
        List<UserRole> authList = new ArrayList<>();
        for (Long roleId : userRole.getRoleIds()) {
            tempUserRole = new UserRole(userRole.getUserId(), roleId);
            tempUserRole.setYnFlag("1");
            tempUserRole.setEditor(UserContext.getCurrentUser().getAccount());
            tempUserRole.setCreator(UserContext.getCurrentUser().getAccount());
            tempUserRole.setCreatedTime(currentDate);
            tempUserRole.setModifiedTime(currentDate);
            authList.add(tempUserRole);
        }
        baseMapper.batchInsertUserRole(authList);
        return new Result(true, null, null, Constants.TOKEN_CHECK_SUCCESS);
    }

    /**
     * 修改密码
     *
     * @param userPassword
     * @return
     */
    @Override
    public Result editPassWord(UserPassword userPassword) {
        if (!StringUtils.isEmpty(userPassword.getPassword()) && !StringUtils.isEmpty(userPassword.getNewPassword())) {
            User user = this.findUserByAccount(UserContext.getCurrentUser().getAccount());
            String encodeNewPassword = ShiroKit.md5(userPassword.getPassword(), SecurityConsts.LOGIN_SALT);
            if (YNFlagStatusEnum.FAIL.getCode().equals(user.getErpFlag())) {
                if (user.getPassword().equals(encodeNewPassword)) {
                    User entity = new User();
                    entity.setPassword(ShiroKit.md5(userPassword.getNewPassword(), SecurityConsts.LOGIN_SALT));
                    entity.setEditor(UserContext.getCurrentUser().getAccount());
                    entity.setEditor(UserContext.getCurrentUser().getAccount());
                    entity.setModifiedTime(Date.from(Instant.now()));
                    QueryWrapper<User> wrapper = new QueryWrapper<>();
                    wrapper.eq("yn_flag", "1");
                    wrapper.eq("account", user.getAccount());
                    baseMapper.update(entity, wrapper);
                    return new Result(true, "修改成功", null, Constants.TOKEN_CHECK_SUCCESS);
                } else {
                    //原始密码错误
                    return new Result(false, "原密码错误", null, Constants.PASSWORD_CHECK_INVALID);
                }
            } else {
                return new Result(false, "参数不完整", null, Constants.PARAMETERS_MISSING);
            }
        }
        return new Result(false, "参数不完整", null, Constants.PARAMETERS_MISSING);
    }
}