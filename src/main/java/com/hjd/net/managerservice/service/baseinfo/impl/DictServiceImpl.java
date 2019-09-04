package com.hjd.net.managerservice.service.baseinfo.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hjd.net.managerservice.config.shiro.security.UserContext;
import com.hjd.net.managerservice.constant.enumtype.YNFlagStatusEnum;
import com.hjd.net.managerservice.entity.baseinfo.Dict;
import com.hjd.net.managerservice.mapper.baseinfo.DictMapper;
import com.hjd.net.managerservice.service.baseinfo.DictService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 字典表 服务实现类
 * </p>
 *
 * @author hujiande
 * @since 2019-07-12
 */
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    @Override
    @Transactional
    public Boolean batchSave(List<Dict> dictList) {
        for(Dict dict: dictList){
            if(dict.getId()!=null){
                this.updateById(dict);
            }else{
                dict.setYnFlag(YNFlagStatusEnum.VALID.getCode());
                dict.setCreatedTime(Date.from(Instant.now()));
                dict.setEditor(UserContext.getCurrentUser().getAccount());
                dict.setCreator(UserContext.getCurrentUser().getAccount());
                this.save(dict);
            }
        }
        return true;
    }
}
