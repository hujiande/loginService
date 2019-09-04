package com.hjd.net.managerservice.service.sysmgr;


import com.baomidou.mybatisplus.extension.service.IService;
import com.hjd.net.managerservice.entity.sysmgr.Resource;
import com.hjd.net.managerservice.entity.vo.Result;
import com.hjd.net.managerservice.entity.vo.sysmgr.ResourceNode;

import java.util.List;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author hujiande
 * @since 2018-12-28
 */
public interface ResourceService extends IService<Resource> {

    /**
     * 查询列表
     * @return
     */
    List<ResourceNode> findAll();

    /**
     * 根据用户查询
     * @param userId
     * @return
     */
    List<ResourceNode> findByUserId(Long userId);


    /**
     * 保存
     * @param resource
     * @return
     */
    Result persist(Resource resource);
}
