package com.hjd.net.managerservice.web;

import com.alibaba.fastjson.JSONObject;
import com.hjd.net.managerservice.config.shiro.security.JwtUtil;
import com.hjd.net.managerservice.constant.Constants;
import com.hjd.net.managerservice.constant.SecurityConsts;
import com.hjd.net.managerservice.entity.sysmgr.User;
import com.hjd.net.managerservice.entity.vo.Result;
import com.hjd.net.managerservice.entity.vo.sysmgr.ResourceNode;
import com.hjd.net.managerservice.entity.vo.sysmgr.UserVo;
import com.hjd.net.managerservice.service.sysmgr.AuthorityService;
import com.hjd.net.managerservice.service.sysmgr.ResourceService;
import com.hjd.net.managerservice.service.sysmgr.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by hujiande
 **/
@Controller
@RequestMapping(value = "/user")
public class LoginController {

    @Autowired
    UserService userService;

    @Autowired
    ResourceService resourceService;

    @Autowired
    AuthorityService authorityService;

    /**
     * 登录
     *
     * @param user
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/login", method = {RequestMethod.POST})
    public Result login(HttpServletResponse response, @RequestBody UserVo user) {
        return userService.login(user, response);
    }

    /**
     * 获取登录用户基础信息
     *
     * @return
     */
    @RequiresAuthentication
    @ResponseBody
    @RequestMapping(value = "/info", method = {RequestMethod.POST, RequestMethod.GET})
    public Result info() {
        Result result = new Result();
        result.setResult(true);
        result.setCode(Constants.TOKEN_CHECK_SUCCESS);
        JSONObject json = new JSONObject();

        User user;
        user = userService.findUserByAccount(JwtUtil.getClaim(SecurityUtils.getSubject().getPrincipal().toString(), SecurityConsts.ACCOUNT));

        json.put("name", user.getName());
        json.put("erp", user.getErpFlag());

        json.put("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        json.put("roles", new String[]{"admin"});

        //查询菜单
        List<ResourceNode> menus = resourceService.findByUserId(user.getId());

        //查询权限
        List<Object> authorityList = authorityService.findByUserId(user.getId());

        json.put("menus", menus);
        json.put("auth", authorityList);

        result.setData(json);
        return result;
    }

}
