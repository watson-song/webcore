## WebCore project:: useful helpers for spring web project [![Maven Central](https://maven-badges.herokuapp.com/maven-central/cn.watsontech/web-core/badge.svg)](https://maven-badges.herokuapp.com/maven-central/cn.watsontech/web-core)

You can use the this project for your spring web project to 

V0.2.1更新
1) 分离各模块，使使用者可以按需加载，具体模块有
    - common    公共工具，不依赖任何项目
    - web-common    web公共组件，包括用户登陆相关
    - mybatis-core  mybatis相关组件，包括代码生成器等
    - openapi-core  开放接口相关，开放接口仅需添加注解即可
    - vendor-file   文件服务，集成了腾讯云存储，华为云存储，阿里云存储服务
    - vendor-push   app推送服务，目前仅集成华为推送
    - verdor-sms    短信服务，支持华为短信、腾讯云短信、阿里云短信服务
    - web-template  springboot的模版项目结构，包括crud和用户登陆

V0.1.5更新

1) 访问日志添加 groupId 分组支持（tb_access_log需添加group_id(varchar)字段）
2) 角色添加 builtinType和 type 支持，原type改成builtinType，新增type与管理员type账号类型匹配（tb_role需添加builtin_type(tinyint(1))字段）

V0.1.5.1更新

1）修复BaseService的 <T> T queryForObject(String sql, Class<T> requiredType, @Nullable Object... args) throws DataAccessException;方法泛型丢弃问题
2）修复openapi时间间隔检查过于严格问题，容易导致时间戳非法


    Gpg版本过高无法弹出密码输入框解决方法（gpg: signing failed: Inappropriate ioctl for device）
    export GPG_TTY=$(tty)
    mvn clean install deploy -P release

