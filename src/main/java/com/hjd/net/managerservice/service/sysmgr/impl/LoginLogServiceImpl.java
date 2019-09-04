package com.hjd.net.managerservice.service.sysmgr.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hjd.net.managerservice.entity.sysmgr.LoginLog;
import com.hjd.net.managerservice.mapper.sysmgr.LoginLogMapper;
import com.hjd.net.managerservice.service.sysmgr.LoginLogService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 登录日志 服务实现类
 * </p>
 *
 * @author hujiande
 * @since 2019-07-26
 */
@Service
public class LoginLogServiceImpl extends ServiceImpl<LoginLogMapper, LoginLog> implements LoginLogService {

}
