package com.hjd.net.managerservice.service.baseinfo;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hjd.net.managerservice.entity.baseinfo.Dict;

import java.util.List;

/**
 * <p>
 * 字典表 服务类
 * </p>
 *
 * @author hujiande
 * @since 2019-07-12
 */
public interface DictService extends IService<Dict> {

    /**
     * 批量保存
     * @param dictList
     * @return
     */
    Boolean batchSave(List<Dict> dictList);
}
