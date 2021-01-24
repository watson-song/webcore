package cn.watsontech.webhelper.openapi.service;


import cn.watsontech.webhelper.openapi.entity.AppInfo;

/**
* Created by Watson Song on 2020/02/12.
*/
public interface OpenAppInfoService {

    AppInfo selectByAppId(String appid);

}