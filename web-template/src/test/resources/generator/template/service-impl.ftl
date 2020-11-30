/*
* ${copyright}
*/
package ${basePackage}.service.impl;

import ${basePackage}.mapper.${modelNameUpperCamel}Mapper;
import ${basePackage}.entity.${modelNameUpperCamel};
import ${basePackage}.service.${modelNameUpperCamel}Service;
import cn.watsontech.webhelper.mybatis.intf.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
* Created by ${author} on ${date}.
*/
@Service
@Transactional
public class ${modelNameUpperCamel}ServiceImpl extends BaseService<${modelNameUpperCamel}, ${primaryKeyType}> implements ${modelNameUpperCamel}Service {

    @Autowired
    public ${modelNameUpperCamel}ServiceImpl(${modelNameUpperCamel}Mapper mapper){
        super(mapper);
    }

}