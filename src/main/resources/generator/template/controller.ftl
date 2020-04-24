/*
* ${copyright}
*/
package ${currentPackage};

import cn.watsontech.core.mybatis.param.TimeRangeCriteriaPageParams;
import cn.watsontech.core.web.result.Result;
import cn.watsontech.core.web.spring.security.LoginUser;
import ${basePackage}.entity.${modelNameUpperCamel};
import ${basePackage}.service.${modelNameUpperCamel}Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by ${author} on ${date}.
*/
@RestController
@RequestMapping("${baseRequestMapping}")
@Api(tags = "${modelNameUpperCamel}Controller", description = "${modelNameUpperCamel}", hidden = true)
public class ${modelNameUpperCamel}Controller {

    @Resource
    private ${modelNameUpperCamel}Service services;

    @ApiOperation(value = "获取${modelNameUpperCamel}列表", response = ${modelNameUpperCamel}.class, responseContainer = "List")
    @GetMapping("/list")
    public Result list(@ModelAttribute TimeRangeCriteriaPageParams params, @AuthenticationPrincipal LoginUser user) {
        Condition condition = wrapCondition(user);
        params.fillCriteria(condition.and());
        condition.setOrderByClause(params.getOrderByClause(${modelNameUpperCamel}.class));

        List<${modelNameUpperCamel}> lists = services.selectByConditionForStartPage(condition, params.getP(), params.getPs());
        long total = services.countByCondition(condition);

        return Result.listResult(lists, params.getP(), params.getPs(), total);
    }

    @ApiOperation(value = "获取${modelNameUpperCamel}详情信息", response = ${modelNameUpperCamel}.class)
    @GetMapping("/{id}")
    public Result details(@PathVariable ${primaryKeyType} id, @AuthenticationPrincipal LoginUser user) {
        ${modelNameUpperCamel} model = services.selectByPrimaryKey(id);
        return Result.successResult(model);
    }

    @ApiOperation(value = "新增${modelNameUpperCamel}", response = Long.class)
    @PostMapping("")
    public Result create(@RequestBody ${modelNameUpperCamel} model, @AuthenticationPrincipal LoginUser user) {
        services.insertSelective(model);
        return Result.successCreateResult(model.getId());
    }

    @ApiOperation(value = "批量新增${modelNameUpperCamel}", response = Integer.class)
    @PostMapping("/batch")
    public Result batchCreate(@RequestBody List<${modelNameUpperCamel}> model, @AuthenticationPrincipal LoginUser user) {
        int success = services.insertList(model);
        return Result.successBaseResult(success);
    }

    @ApiOperation(value = "修改${modelNameUpperCamel}信息", response = Integer.class)
    @PutMapping("/{id}")
    public Result update(@PathVariable ${primaryKeyType} id, @RequestBody ${modelNameUpperCamel} model, @AuthenticationPrincipal LoginUser user) {
        int success = services.updateByPrimaryKeySelective(model);
        return Result.successBaseResult(success);
    }

    @ApiOperation(value = "删除${modelNameUpperCamel}", response = Integer.class)
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable ${primaryKeyType} id, @AuthenticationPrincipal LoginUser user) {
        int success = services.deleteByPrimaryKey(id);
        return Result.successBaseResult(success);
    }

    @ApiOperation(value = "批量删除${modelNameUpperCamel}", response = Integer.class)
    @DeleteMapping("/batch")
    public Result batchDelete(@RequestBody List<${primaryKeyType}> ids, @AuthenticationPrincipal LoginUser user) {
        int success = services.deleteByIds(ids);
        return Result.successBaseResult(success);
    }

    @ApiOperation(value = "禁用${modelNameUpperCamel}", response = Integer.class)
    @PutMapping("/{id}/disable")
    public Result disable(@PathVariable Long id, @AuthenticationPrincipal LoginUser user) {
        ${modelNameUpperCamel} model = new ${modelNameUpperCamel}();
        model.setId(id);
        model.setEnabled(false);
        model.setModifiedBy(user.getId());
        return Result.successBaseResult(services.updateByPrimaryKeySelective(model));
    }

    @ApiOperation(value = "启用${modelNameUpperCamel}", response = Integer.class)
    @PutMapping("/{id}/enable")
    public Result enable(@PathVariable Long id, @AuthenticationPrincipal LoginUser user) {
        ${modelNameUpperCamel} model = new ${modelNameUpperCamel}();
        model.setId(id);
        model.setEnabled(true);
        model.setModifiedBy(user.getId());
        return Result.successBaseResult(services.updateByPrimaryKeySelective(model));
    }

    private Condition wrapCondition(LoginUser user) {
        Condition condition = new Condition(${modelNameUpperCamel}.class);
        condition.excludeProperties("modifiedBy", "modifiedTime", "createdByName", "createdBy", "enabled", "version");
        condition.createCriteria().andEqualTo("enabled", true);
        return condition;
    }
}