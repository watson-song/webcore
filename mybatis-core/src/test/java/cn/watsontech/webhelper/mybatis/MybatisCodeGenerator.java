package cn.watsontech.webhelper.mybatis;

import cn.watsontech.webhelper.mybatis.generator.CodeGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 生成数据库Mapper和Service、Controller代码工具方法
 * Created by Watson on 2020/4/24.
 */
public class MybatisCodeGenerator {

    public static void main(String[] args) {
        String basePackage = "xxx.xx.xxx";
        String jdbcHost = "192.168.3.173:3306";
        String jdbcDb = "dbname";
        String jdbcUsername = "root";
        String jdbcPassword = "111111";
        String author = "Your Name";
        String orgName = "Your Group Name";
        List<CodeGenerator.TableModelParam> tableModelParams = new ArrayList<>();
        CodeGenerator codeGenerator = new CodeGenerator(basePackage, jdbcHost, jdbcDb, jdbcUsername, jdbcPassword, author, orgName, tableModelParams);

        addTableModeParams(tableModelParams);

        CodeGenerator.TableModelParam[] tableParams = new CodeGenerator.TableModelParam[tableModelParams.size()];
        tableModelParams.toArray(tableParams);
        codeGenerator.generateJavaCodes(tableParams);
    }

    private static void addTableModeParams(List<CodeGenerator.TableModelParam> tableModelParams) {

        /**
         * 1、若有除了admin和user之外的账号体系，需设置 enabled 属性为 isEnabled
         * ColumnOverride columnOverride = new ColumnOverride("enabled");
         * columnOverride.setJavaProperty("isEnabled");
         *
         * 2、若有Json类型字段，请使用
         * JSON类型：CodeGenerator.wrapColumnOverride("columnName", "com.alibaba.fastjson.JSON", "cn.watsontech.core.mybatis.handler.MySqlJSONTypeHandler")
         * JSONObject类型：CodeGenerator.wrapColumnOverride("columnName", "com.alibaba.fastjson.JSONObject", "cn.watsontech.core.mybatis.handler.MySqlJSONObjectTypeHandler")
         * JSONArray类型：CodeGenerator.wrapColumnOverride("columnName", "com.alibaba.fastjson.JSONArray", "cn.watsontech.core.mybatis.handler.MySqlJSONArrayTypeHandler")
         */
        tableModelParams.add(new CodeGenerator.TableModelParam("tb_staff", "Staff", "Long", "/api/v1/base", false));
        tableModelParams.add(new CodeGenerator.TableModelParam("tb_stall_status", "StallAlertEventStatus", "Long", "/api/v1/base", Arrays.asList(CodeGenerator.wrapColumnOverride("event_data", "com.alibaba.fastjson.JSON", "cn.watsontech.core.mybatis.handler.MySqlJSONTypeHandler"), CodeGenerator.wrapColumnOverride("handle_data", "com.alibaba.fastjson.JSON", "cn.watsontech.core.mybatis.handler.MySqlJSONTypeHandler")), false));
    }
}
