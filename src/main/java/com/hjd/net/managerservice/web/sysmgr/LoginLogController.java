package com.hjd.net.managerservice.web.sysmgr;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hjd.net.managerservice.config.shiro.security.UserContext;
import com.hjd.net.managerservice.constant.Constants;
import com.hjd.net.managerservice.entity.sysmgr.LoginLog;
import com.hjd.net.managerservice.entity.sysmgr.User;
import com.hjd.net.managerservice.entity.vo.Result;
import com.hjd.net.managerservice.service.sysmgr.LoginLogService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;

/**
 * <p>
 * 登录日志 前端控制器
 * </p>
 *
 * @author hujiande
 * @since 2019-07-26
 */
@RestController
@RequestMapping("/sysmgr/loginlog")
public class LoginLogController {

    @Autowired
    LoginLogService loginLogService;

    /**
     * 分页查询
     * @param loginLog
     * @param pageNo
     * @param limit
     * @return
     */
    @RequiresPermissions("sysmgr.loginlog.query")
    @RequestMapping(value="/list",method = {RequestMethod.POST,RequestMethod.GET})
    public Result list(LoginLog loginLog,
                       @RequestParam(defaultValue = "1")int pageNo,
                       @RequestParam(defaultValue = "10")int limit){
        Result result = new Result();
        Page<LoginLog> page = new Page(pageNo, limit);
        QueryWrapper<LoginLog> eWrapper = new QueryWrapper(loginLog);
        eWrapper.eq("yn_flag","1");
        IPage<LoginLog> list = loginLogService.page(page, eWrapper);
        result.setData(list);
        result.setResult(true);
        result.setCode(Constants.TOKEN_CHECK_SUCCESS);
        return result;
    }

    /**
     * 删除
     * @param user
     * @return
     */
    @RequiresPermissions("sysmgr.loginlog.delete")
    @RequestMapping(value="/delete",method = {RequestMethod.POST})
    public Result dropById(@RequestBody User user){
        Result result ;
        if(user.getId()!=null){
            LoginLog delLog= new LoginLog();
            delLog.setId(user.getId());
            delLog.setYnFlag("0");
            delLog.setEditor(UserContext.getCurrentUser().getAccount());
            delLog.setModifiedTime(Date.from(Instant.now()));
            result=new Result(loginLogService.updateById(delLog),null,null,Constants.TOKEN_CHECK_SUCCESS);
        }else{
            result = new Result(false, "", null ,Constants.PARAMETERS_MISSING);
        }
        return result;
    }

}
