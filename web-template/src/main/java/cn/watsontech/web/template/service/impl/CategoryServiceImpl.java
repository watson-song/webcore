/*
* Copyright (c) 2020.  -- 
*/
package cn.watsontech.web.template.service.impl;

import cn.watsontech.web.template.entity.Category;
import cn.watsontech.web.template.mapper.CategoryMapper;
import cn.watsontech.web.template.service.CategoryService;
import cn.watsontech.webhelper.mybatis.intf.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
* Created by Your name on 2020/11/30.
*/
@Service
@Transactional
public class CategoryServiceImpl extends BaseService<Category, Long> implements CategoryService {

    @Autowired
    public CategoryServiceImpl(CategoryMapper mapper){
        super(mapper);
    }

}