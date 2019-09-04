package com.hjd.net.managerservice.service.sysmgr;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hjd.net.managerservice.entity.sysmgr.Role;
import com.hjd.net.managerservice.entity.vo.Result;
import com.hjd.net.managerservice.entity.vo.sysmgr.RoleAuth;

import java.util.List;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author hujiande
 * @since 2018-12-28
 */
public interface RoleService extends IService<Role> {

    /**
     * 根据用户ID查询角色
     *
     * @param userId
     * @return
     */
    List<Role> findRoleByUserId(Long userId);

    /**
     * 保存角色
     *
     * @param role
     * @return
     */
    Result persist(Role role);

    /**
     * 修改权限
     * @param roleAuth
     * @return
     */
    Result saveRoleAuths(RoleAuth roleAuth);

    /**
     * 根据角色ID获取权限
     * @param roleId
     * @return
     */
    Result selectAuthByRoleId(Long roleId);


}
