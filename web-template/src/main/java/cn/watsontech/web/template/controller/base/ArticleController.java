/*
* Copyright (c) 2020.  -- 
*/
package cn.watsontech.web.template.controller.base;

import cn.watsontech.webhelper.common.result.Result;
import cn.watsontech.webhelper.common.security.LoginUser;
import cn.watsontech.webhelper.mybatis.param.TimeRangeCriteriaPageParams;
import cn.watsontech.web.template.entity.Article;
import cn.watsontech.web.template.service.ArticleService;
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
@RequestMapping("/api/v1/admin/articles")
@Api(tags = "ArticleController", description = "Article", hidden = true)
public class ArticleController {

    @Resource
    private ArticleService services;

    @ApiOperation(value = "获取Article列表", response = Article.class, responseContainer = "List")
    @GetMapping("/list")
    public Result list(@ModelAttribute TimeRangeCriteriaPageParams params, @AuthenticationPrincipal LoginUser user) {
        Condition condition = wrapCondition(user);
        params.fillCriteria(condition.and());
        condition.setOrderByClause(params.getOrderByClause(Article.class));

        List<Article> lists = services.selectByConditionForOffsetAndLimit(condition, params.getOffset(), params.getLimit(), true);
        return Result.pageResult(lists);
    }

    @ApiOperation(value = "获取Article详情信息", response = Article.class)
    @GetMapping("/{id}")
    public Result details(@PathVariable Long id, @AuthenticationPrincipal LoginUser user) {
        Article model = services.selectByPrimaryKey(id);
        return Result.successResult(model);
    }

    @ApiOperation(value = "新增Article", response = Long.class)
    @PostMapping("")
    public Result create(@RequestBody Article model, @AuthenticationPrincipal LoginUser user) {
        services.insertSelective(model);
        return Result.successCreateResult(model.getId());
    }

    @ApiOperation(value = "批量新增Article", response = Integer.class)
    @PostMapping("/batch")
    public Result batchCreate(@RequestBody List<Article> model, @AuthenticationPrincipal LoginUser user) {
        int success = services.insertList(model);
        return Result.successBaseResult(success);
    }

    @ApiOperation(value = "修改Article信息", response = Integer.class)
    @PutMapping("/{id}")
    public Result update(@PathVariable Long id, @RequestBody Article model, @AuthenticationPrincipal LoginUser user) {
        int success = services.updateByPrimaryKeySelective(model);
        return Result.successBaseResult(success);
    }

    @ApiOperation(value = "删除Article", response = Integer.class)
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id, @AuthenticationPrincipal LoginUser user) {
        int success = services.deleteByPrimaryKey(id);
        return Result.successBaseResult(success);
    }

    @ApiOperation(value = "批量删除Article", response = Integer.class)
    @DeleteMapping("/batch")
    public Result batchDelete(@RequestBody List<Long> ids, @AuthenticationPrincipal LoginUser user) {
        int success = services.deleteByIds(ids);
        return Result.successBaseResult(success);
    }

    private Condition wrapCondition(LoginUser user) {
        Condition condition = new Condition(Article.class);
        condition.excludeProperties("modifiedBy", "modifiedTime", "version");
        return condition;
    }
}