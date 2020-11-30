/*
* Copyright (c) 2020.  -- 
*/
package cn.watsontech.web.template.controller.base;

import cn.watsontech.web.template.entity.Admin;
import cn.watsontech.web.template.service.AdminService;
import cn.watsontech.webhelper.common.result.Result;
import cn.watsontech.webhelper.common.security.LoginUser;
import cn.watsontech.webhelper.mybatis.param.TimeRangeCriteriaPageParams;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by Your name on 2020/11/30.
*/
@RestController
@RequestMapping("/api/v1/admin/accounts")
@Api(tags = "AdminController", description = "Admin", hidden = true)
public class AdminController {

    @Resource
    private AdminService services;

    @ApiOperation(value = "获取Admin列表", response = Admin.class, responseContainer = "List")
    @GetMapping("/list")
    public Result list(@ModelAttribute TimeRangeCriteriaPageParams params, @AuthenticationPrincipal LoginUser user) {
        Condition condition = wrapCondition(user);
        params.fillCriteria(condition.and());
        condition.setOrderByClause(params.getOrderByClause(Admin.class));

        List<Admin> lists = services.selectByConditionForOffsetAndLimit(condition, params.getOffset(), params.getLimit(), true);
        return Result.pageResult(lists);
    }

    @ApiOperation(value = "获取Admin详情信息", response = Admin.class)
    @GetMapping("/{id}")
    public Result details(@PathVariable Long id, @AuthenticationPrincipal LoginUser user) {
        Admin model = services.selectByPrimaryKey(id);
        return Result.successResult(model);
    }

    @ApiOperation(value = "新增Admin", response = Long.class)
    @PostMapping("")
    public Result create(@RequestBody Admin model, @AuthenticationPrincipal LoginUser user) {
        services.insertSelective(model);
        return Result.successCreateResult(model.getId());
    }

    @ApiOperation(value = "批量新增Admin", response = Integer.class)
    @PostMapping("/batch")
    public Result batchCreate(@RequestBody List<Admin> model, @AuthenticationPrincipal LoginUser user) {
        int success = services.insertList(model);
        return Result.successBaseResult(success);
    }

    @ApiOperation(value = "修改Admin信息", response = Integer.class)
    @PutMapping("/{id}")
    public Result update(@PathVariable Long id, @RequestBody Admin model, @AuthenticationPrincipal LoginUser user) {
        int success = services.updateByPrimaryKeySelective(model);
        return Result.successBaseResult(success);
    }

    @ApiOperation(value = "删除Admin", response = Integer.class)
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id, @AuthenticationPrincipal LoginUser user) {
        int success = services.deleteByPrimaryKey(id);
        return Result.successBaseResult(success);
    }

    @ApiOperation(value = "批量删除Admin", response = Integer.class)
    @DeleteMapping("/batch")
    public Result batchDelete(@RequestBody List<Long> ids, @AuthenticationPrincipal LoginUser user) {
        int success = services.deleteByIds(ids);
        return Result.successBaseResult(success);
    }

    private Condition wrapCondition(LoginUser user) {
        Condition condition = new Condition(Admin.class);
        condition.excludeProperties("modifiedBy", "modifiedTime", "version");
        return condition;
    }
}