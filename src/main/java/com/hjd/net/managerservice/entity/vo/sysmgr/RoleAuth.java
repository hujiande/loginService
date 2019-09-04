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
public class RoleAuth {
    private Long roleId;
    private Set<Long> authorityIds;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Set<Long> getAuthorityIds() {
        return authorityIds;
    }

    public void setAuthorityIds(Set<Long> authorityIds) {
        this.authorityIds = authorityIds;
    }
}
