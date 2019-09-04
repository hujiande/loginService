package com.hjd.net.managerservice.entity.vo.sysmgr;

import lombok.Data;

import java.util.Set;

/**
 * <p>
 * </p>
 *
 * @author hujiande
 * @since 2018-12-28
 */
@Data
public class UserRoleVo {
    private Long userId;
    private Set<Long> roleIds;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Set<Long> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(Set<Long> roleIds) {
        this.roleIds = roleIds;
    }
}
