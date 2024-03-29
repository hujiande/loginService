package com.hjd.net.managerservice.web.sysmgr;

import com.hjd.net.managerservice.config.shiro.security.UserContext;
import com.hjd.net.managerservice.constant.Constants;
import com.hjd.net.managerservice.entity.sysmgr.Resource;
import com.hjd.net.managerservice.entity.vo.Result;
import com.hjd.net.managerservice.entity.vo.sysmgr.ResourceNode;
import com.hjd.net.managerservice.service.sysmgr.ResourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Api(description = "菜单接口")
@RestController
@RequestMapping(value="/sysmgr/resource")
public class ResourceController {
    @Autowired
    ResourceService ResourceService;

    /**
     * 查询所有菜单
     * @return
     */
    @ApiOperation(value = "所有菜单" ,  notes="查询所有菜单")
    @RequiresPermissions("sysmgr.resource.query")
    @RequestMapping(value="/list",method = {RequestMethod.POST,RequestMethod.GET})
    public Result list(){
        List<ResourceNode> trees = ResourceService.findAll();

        Result result = new Result();
        result.setData(trees);
        result.setResult(true);
        result.setCode(Constants.TOKEN_CHECK_SUCCESS);
        return result;
    }

    /**
     * 保存
     * @param resource
     * @return
     */
    @RequiresPermissions("sysmgr.resource.save")
    @RequestMapping(value="/save",method = {RequestMethod.POST})
    public Result save(@RequestBody Resource resource){
        return ResourceService.persist(resource);
    }

    /**
     * 删除
     * @param resource
     * @return
     */
    @RequiresPermissions("sysmgr.resource.delete")
    @RequestMapping(value="/delete",method = {RequestMethod.POST})
    public Result dropById(@RequestBody Resource resource){
        Result result ;
        if(resource.getId()!=null){
            Resource delRes= new Resource();
            delRes.setId(resource.getId());
            delRes.setYnFlag("0");
            delRes.setEditor(UserContext.getCurrentUser().getAccount());
            delRes.setModifiedTime(Date.from(Instant.now()));
            result=new Result(ResourceService.updateById(delRes),null,null,Constants.TOKEN_CHECK_SUCCESS);
        }else{
            result = new Result(false, "", null ,Constants.PARAMETERS_MISSING);
        }
        return result;
    }
}
