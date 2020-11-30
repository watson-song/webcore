package cn.watsontech.web.template;


import cn.watsontech.webhelper.mybatis.generator.CodeGenerator;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Watson on 2020/4/24.
 */
public class MybatisCodeGenerator {

    public static void main(String[] args) {
        System.setProperty("user.dir", System.getProperty("user.dir")+ File.separatorChar+"api-web-template");

        String basePackage = "cn.watsontech.web.template";
        String jdbcHost = "localhost:3306";
        String jdbcDb = "template_project";
        String jdbcUsername = "root";
        String jdbcPassword = "root";
        String author = "Your name";
        String orgName = " -- ";
        List<CodeGenerator.TableModelParam> tableModelParams = new ArrayList<>();
        CodeGenerator codeGenerator = new CodeGenerator(basePackage, jdbcHost, jdbcDb, jdbcUsername, jdbcPassword, author, orgName, tableModelParams);

        addTableModeParams(tableModelParams);

        CodeGenerator.TableModelParam[] tableParams = new CodeGenerator.TableModelParam[tableModelParams.size()];
        tableModelParams.toArray(tableParams);
        codeGenerator.generateJavaCodes(tableParams);
    }

    private static void addTableModeParams(List<CodeGenerator.TableModelParam> tableModelParams) {
        String apiUrlPrefix = "/api/v1/admin";

        tableModelParams.add(new CodeGenerator.TableModelParam("tb_admin", "Admin", "Long", apiUrlPrefix));
        tableModelParams.add(new CodeGenerator.TableModelParam("tb_article", "Article", "Long", apiUrlPrefix));

        //handle_data 字段为json格式
        tableModelParams.add(new CodeGenerator.TableModelParam("tb_category", "Category", "Long", apiUrlPrefix, Arrays.asList(CodeGenerator.wrapColumnOverride("extra_data", "com.alibaba.fastjson.JSON", "cn.watsontech.webhelper.mybatis.handler.MySqlJSONTypeHandler"))));

    }
}
