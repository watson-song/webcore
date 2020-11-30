/*
* Copyright (c) 2020.  -- 
*/
package cn.watsontech.web.template.service.impl;

import cn.watsontech.web.template.mapper.ArticleMapper;
import cn.watsontech.web.template.service.ArticleService;
import cn.watsontech.web.template.entity.Article;
import cn.watsontech.webhelper.mybatis.intf.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
* Created by Your name on 2020/11/30.
*/
@Service
@Transactional
public class ArticleServiceImpl extends BaseService<Article, Long> implements ArticleService {

    @Autowired
    public ArticleServiceImpl(ArticleMapper mapper){
        super(mapper);
    }

}