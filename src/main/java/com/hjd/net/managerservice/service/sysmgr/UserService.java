package com.hjd.net.managerservice.service.sysmgr;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hjd.net.managerservice.entity.sysmgr.User;
import com.hjd.net.managerservice.entity.vo.Result;
import com.hjd.net.managerservice.entity.vo.sysmgr.UserPassword;
import com.hjd.net.managerservice.entity.vo.sysmgr.UserRoleVo;
import com.hjd.net.managerservice.entity.vo.sysmgr.UserVo;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author hujiande
 * @since 2018-12-28
 */
public interface UserService extends IService<User> {

    /**
     * 根据用户账号查询用户详情
     * @param account
     * @return
     */
    User findUserByAccount(String account);

    /**
     * 用户登录
     * @param user
     * @return
     */
    Result login(UserVo user, HttpServletResponse response);

    /**
     * 保存用户
     * @param user
     * @return
     */
    Result persist(User user);

    /**
     * 获取用户ID角色
     * @param userId
     * @return
     */
    Result findUserRole(Long userId);

    /**
     * 修改用户角色
     * @param userRole
     * @return
     */
    Result saveUserRoles(UserRoleVo userRole);

    /**
     * 修改用户密码
     * @param userPassword
     * @return
     */
    Result editPassWord(UserPassword userPassword);

}
