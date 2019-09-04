package com.hjd.net.managerservice.service.sysmgr.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hjd.net.managerservice.entity.sysmgr.RoleAuthority;
import com.hjd.net.managerservice.entity.sysmgr.UserRole;
import com.hjd.net.managerservice.mapper.sysmgr.RoleAuthorityMapper;
import com.hjd.net.managerservice.mapper.sysmgr.UserRoleMapper;
import com.hjd.net.managerservice.service.sysmgr.RoleAuthorityService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 权限表 服务实现类
 * </p>
 *
 * @author hujiande
 * @since 2018-12-28
 */
@Service
public class RoleAuthorityServiceImpl extends ServiceImpl<RoleAuthorityMapper, RoleAuthority> implements RoleAuthorityService {

    @Resource
    protected RoleAuthorityMapper roleAuthorityMapper;

    @Resource
    protected UserRoleMapper userRoleMapper;

    /**
     * 批量新增
     * @param list
     */
    @Override
    public void batchInsert(List<RoleAuthority> list) {
        baseMapper.batchInsert(list);
    }

    /**
     * 根据角色删除
     * @param role
     */
    @Override
    public void deleteAuthByRoleId(RoleAuthority role) {
        baseMapper.deleteAuthByRoleId(role);
    }

    /**
     * 根据角色查询
     * @param roleId
     * @return
     */
    @Override
    public List<Long> selectAuthByRoleId(Long roleId) {
        return baseMapper.selectAuthByRoleId(roleId);
    }

    @Override
    public List<RoleAuthority> findByUserId(Long userId) {
        //获取角色
        QueryWrapper<UserRole> userRoleWrapper= new QueryWrapper<>();
        userRoleWrapper.eq("user_id",userId);
        userRoleWrapper.eq("yn_flag","1");
        List<UserRole> userRoleList= userRoleMapper.selectList(userRoleWrapper);

        //获取角色权限
        List<RoleAuthority> roleAuthList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(userRoleList)){
            QueryWrapper<RoleAuthority> roleAuthWrapper= new QueryWrapper<>();
            roleAuthWrapper.eq("yn_flag","1");
            roleAuthWrapper.in("role_id",userRoleList.stream().map(e -> e.getRoleId()).collect(Collectors.toSet()));
            roleAuthList= roleAuthorityMapper.selectList(roleAuthWrapper);
        }
        return roleAuthList;
    }
}
