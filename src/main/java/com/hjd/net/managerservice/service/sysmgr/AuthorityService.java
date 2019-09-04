package com.hjd.net.managerservice.service.sysmgr;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hjd.net.managerservice.entity.sysmgr.Authority;
import com.hjd.net.managerservice.entity.vo.Result;
import com.hjd.net.managerservice.entity.vo.sysmgr.AuthorityNode;

import java.util.List;

/**
 * <p>
 * 权限表 服务类
 * </p>
 *
 * @author hujiande
 * @since 2018-12-28
 */
public interface AuthorityService extends IService<Authority> {

    /**
     * 查询列表
     * @return
     */
    List<AuthorityNode> findAll();

    /**
     * 根据用户查询
     * @param userId
     * @return
     */
    List<Object> findByUserId(Long userId);

    /**
     * 保存
     * @param resource
     * @return
     */
    Result persist(Authority resource);
}
