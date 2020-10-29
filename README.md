## WebCore project:: useful helpers for spring web project [![Maven Central](https://maven-badges.herokuapp.com/maven-central/cn.watsontech/web-core/badge.svg)](https://maven-badges.herokuapp.com/maven-central/cn.watsontech/web-core)

You can use the this project for your spring web project to 


V0.1.5更新

1) 访问日志添加 groupId 分组支持（tb_access_log需添加group_id(varchar)字段）
2) 角色添加 builtinType和 type 支持，原type改成builtinType，新增type与管理员type账号类型匹配（tb_role需添加builtin_type(tinyint(1))字段）

V0.1.5.1更新

1）修复BaseService的 <T> T queryForObject(String sql, Class<T> requiredType, @Nullable Object... args) throws DataAccessException;方法泛型丢弃问题
2）修复openapi时间间隔检查过于严格问题，容易导致时间戳非法
