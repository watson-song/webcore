package cn.watsontech.core.mybatis.generator;

import cn.watsontech.core.mybatis.util.Inflector;
import cn.watsontech.core.web.spring.util.Assert;
import com.google.common.base.CaseFormat;
import freemarker.template.TemplateExceptionHandler;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.*;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 代码生成器，根据数据表名称生成对应的Model、Mapper、Service、Controller简化开发。
 *
 * Created by Watson on 2020/04/24.
 */
public class CodeGenerator {
    final String BASE_PACKAGE;//项目基础包名称，根据自己的项目修改

    /*生成文件地址配置*/
    final String MODEL_PACKAGE;//生成的Model类所在包
    final String MAPPER_PACKAGE;//生成的Mapper所在包
    final String SERVICE_PACKAGE;//生成的Service所在包
    final String SERVICE_IMPL_PACKAGE;//生成的ServiceImpl所在包
    final String CONTROLLER_PACKAGE;//生成的Controller所在包

    /*数据库配置*/
    final String JDBC_URL;//数据库url
    final String JDBC_HOST;
    final String JDBC_DB;
    final String JDBC_USERNAME;
    final String JDBC_PASSWORD;
    final String JDBC_DIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";

    final String PACKAGE_PATH_SERVICE;//生成的Service存放路径
    final String PACKAGE_PATH_SERVICE_IMPL;//生成的Service实现存放路径
    final String PACKAGE_PATH_CONTROLLER;//生成的Controller存放路径

    final String AUTHOR;//@author
    final String DATE = new SimpleDateFormat("yyyy/MM/dd").format(new Date());//@date
    final String COPYRIGHT;//@author

    //需要生成代码的表参数
    final List<TableModelParam> tableModelParams;

    /*可通过set方法配置参数*/
    String MAPPER_INTERFACE_REFERENCE = "cn.watsontech.core.mybatis.Mapper";//Mapper插件基础接口的完全限定名(第二步提到的核心继承接口Mapper)
    String PROJECT_PATH = System.getProperty("user.dir");//项目在硬盘上的基础路径
    String TEMPLATE_FILE_PATH = PROJECT_PATH + "/src/test/main/resources/generator/template";//模板位置
    String TARGET_JAVA_PATH = "/src/main/java"; //java文件路径
    String RESOURCES_PATH = "/src/main/resources";//资源文件路径

    /**
     * 代码自动生成器
     * @param basePackage 基本包名
     * @param tableModelParams 需要生成代码的表参数
     */
    public CodeGenerator(String basePackage, String jdbcHost, String jdbcDb, String jdbcUsername, String jdbcPassword, String author, String orgName, List<TableModelParam> tableModelParams) {
        this.BASE_PACKAGE = basePackage;

        /*生成文件地址配置*/
        this.MODEL_PACKAGE = BASE_PACKAGE + ".entity";//生成的Model类所在包
        this.MAPPER_PACKAGE = BASE_PACKAGE + ".mapper";//生成的Mapper所在包
        this.SERVICE_PACKAGE = BASE_PACKAGE + ".service";//生成的Service所在包
        this.SERVICE_IMPL_PACKAGE = SERVICE_PACKAGE + ".impl";//生成的ServiceImpl所在包
        this.CONTROLLER_PACKAGE = BASE_PACKAGE + ".controller.base";//生成的Controller所在包
        PACKAGE_PATH_SERVICE = packageConvertPath(SERVICE_PACKAGE);//生成的Service存放路径
        PACKAGE_PATH_SERVICE_IMPL = packageConvertPath(SERVICE_IMPL_PACKAGE);//生成的Service实现存放路径
        PACKAGE_PATH_CONTROLLER = packageConvertPath(CONTROLLER_PACKAGE);//生成的Controller存放路径

        /*数据库配置*/
        this.JDBC_HOST = jdbcHost;
        this.JDBC_DB = jdbcDb;
        this.JDBC_USERNAME = jdbcUsername;
        this.JDBC_PASSWORD = jdbcPassword;
        this.JDBC_URL = "jdbc:mysql://"+this.JDBC_HOST+"/"+this.JDBC_DB+"?useSSL=false&nullCatalogMeansCurrent=true";//数据库url

        this.COPYRIGHT = "Copyright (c) "+ DateFormatUtils.format(new Date(), "yyyy")+". " + orgName;
        this.AUTHOR = author;
        this.tableModelParams = tableModelParams;
    }

    public static class TableModelParam {
        String tableName;
        String modelName;
        String apiPrefix;
        String primaryKeyType;

        boolean withSwagger = true; //是否自带swagger注解
        boolean beanBuilderMode = true; //是否启用entity的builder模式
        boolean isGeneratedKey = true; //是否为自增id

        List<ColumnOverride> columnOverrides;
        List<IgnoredColumn> ignoredColumns;

        public TableModelParam(String tableName) {
            this.tableName = tableName;
        }

        public TableModelParam(String tableName, String modelName) {
            this.tableName = tableName;
            this.modelName = modelName;
        }

        public TableModelParam(String tableName, String modelName, String primaryKeyType) {
            this.tableName = tableName;
            this.modelName = modelName;
            this.primaryKeyType = primaryKeyType;
        }

        public TableModelParam(String tableName, String modelName, String primaryKeyType, String apiPrefix) {
            this.tableName = tableName;
            this.modelName = modelName;
            this.primaryKeyType = primaryKeyType;
            this.apiPrefix = apiPrefix;
        }

        public TableModelParam(String tableName, String modelName, String primaryKeyType, String apiPrefix, boolean isGeneratedKey) {
            this.tableName = tableName;
            this.modelName = modelName;
            this.primaryKeyType = primaryKeyType;
            this.apiPrefix = apiPrefix;
            this.isGeneratedKey = isGeneratedKey;
        }

        public TableModelParam(String tableName, String modelName, String primaryKeyType, String apiPrefix, List<ColumnOverride> columnOverrides) {
            this.tableName = tableName;
            this.modelName = modelName;
            this.apiPrefix = apiPrefix;
            this.primaryKeyType = primaryKeyType;
            this.columnOverrides = columnOverrides;
        }

        public TableModelParam(String tableName, String modelName, String primaryKeyType, String apiPrefix, List<ColumnOverride> columnOverrides, boolean isGeneratedKey) {
            this.tableName = tableName;
            this.modelName = modelName;
            this.apiPrefix = apiPrefix;
            this.primaryKeyType = primaryKeyType;
            this.columnOverrides = columnOverrides;
            this.isGeneratedKey = isGeneratedKey;
        }

        public TableModelParam(String tableName, String modelName, String primaryKeyType, String apiPrefix, List<ColumnOverride> columnOverrides, List<IgnoredColumn> ignoredColumns) {
            this.tableName = tableName;
            this.modelName = modelName;
            this.apiPrefix = apiPrefix;
            this.primaryKeyType = primaryKeyType;
            this.columnOverrides = columnOverrides;
            this.ignoredColumns = ignoredColumns;
        }

        public TableModelParam(String tableName, String modelName, String primaryKeyType, String apiPrefix, List<ColumnOverride> columnOverrides, List<IgnoredColumn> ignoredColumns, boolean isGeneratedKey) {
            this.tableName = tableName;
            this.modelName = modelName;
            this.apiPrefix = apiPrefix;
            this.primaryKeyType = primaryKeyType;
            this.columnOverrides = columnOverrides;
            this.ignoredColumns = ignoredColumns;
            this.isGeneratedKey = isGeneratedKey;
        }

        public String getTableName() {
            return tableName;
        }

        public String getModelName() {
            return modelName;
        }

        public String getPrimaryKeyType() {
            return primaryKeyType;
        }

        public String getApiPrefix() {
            return apiPrefix;
        }

        public List<ColumnOverride> getColumnOverrides() {
            return columnOverrides;
        }

        public List<IgnoredColumn> getIgnoredColumns() {
            return ignoredColumns;
        }

        public TableModelParam setWithSwagger(boolean withSwagger) {
            this.withSwagger = withSwagger;
            return this;
        }

        public TableModelParam setBeanBuilderMode(boolean beanBuilderMode) {
            this.beanBuilderMode = beanBuilderMode;
            return this;
        }

        public boolean isWithSwagger() {
            return withSwagger;
        }

        public boolean isBeanBuilderMode() {
            return beanBuilderMode;
        }

        public boolean isGeneratedKey() {
            return isGeneratedKey;
        }
    }

    public static ColumnOverride wrapColumnOverride(String column, String javaType) {
        ColumnOverride columnOverride = new ColumnOverride(column);
        columnOverride.setJavaType(javaType);
        return columnOverride;
    }

    public static ColumnOverride wrapColumnOverride(String column, String javaType, String typeHandler) {
        ColumnOverride columnOverride = new ColumnOverride(column);
        columnOverride.setJavaType(javaType);
        columnOverride.setTypeHandler(typeHandler);
        return columnOverride;
    }

    public static IgnoredColumn wrapIgnoredColumn(String column) {
        return new IgnoredColumn(column);
    }

    public void setPROJECT_PATH(String PROJECT_PATH) {
        this.PROJECT_PATH = PROJECT_PATH;
    }

    public void setTEMPLATE_FILE_PATH(String TEMPLATE_FILE_PATH) {
        this.TEMPLATE_FILE_PATH = TEMPLATE_FILE_PATH;
    }

    public void setTARGET_JAVA_PATH(String TARGET_JAVA_PATH) {
        this.TARGET_JAVA_PATH = TARGET_JAVA_PATH;
    }

    public void setRESOURCES_PATH(String RESOURCES_PATH) {
        this.RESOURCES_PATH = RESOURCES_PATH;
    }

    public void setMAPPER_INTERFACE_REFERENCE(String MAPPER_INTERFACE_REFERENCE) {
        this.MAPPER_INTERFACE_REFERENCE = MAPPER_INTERFACE_REFERENCE;
    }
    /**
     * 通过数据表名称生成代码，Model 名称通过解析数据表名称获得，下划线转大驼峰的形式。
     * 如输入表名称 "t_user_detail" 将生成 TUserDetail、TUserDetailMapper、TUserDetailService ...
     * @param tableParams 数据表名称...
     * 正常参数：new TableModelParam("tb_role", "Role", "Long", "/api/v1/base")
     * 带JSON的表参数：new TableModelParam("tb_permission", "Permission", "Long", "/api/v1/base", Arrays.asList(wrapColumnOverride("list", "com.alibaba.fastjson.JSON", "cn.watsontech.core.mybatis.handler.MySqlJSONTypeHandler"))),
     * 带JSONArray的表参数：new TableModelParam("tb_push_token", "PushToken", "Long", "/api/v1/base", Arrays.asList(wrapColumnOverride("topics", "com.alibaba.fastjson.JSONArray", "cn.watsontech.core.mybatis.handler.MySqlJSONArrayTypeHandler"))),
     * 带参数类型转换和JSONObject的表参数：new TableModelParam("tb_admin", "Admin", "Long", "/api/v1/base", Arrays.asList(wrapColumnOverride("enabled", "boolean"), wrapColumnOverride("extra_data", "com.alibaba.fastjson.JSONObject", "cn.watsontech.core.mybatis.handler.MySqlJSONObjectTypeHandler")))
     */
    public void generateJavaCodes(TableModelParam... tableParams) {
        Assert.notNull(JDBC_HOST, "数据库未配置：jdbc_host");
        Assert.notNull(JDBC_DB, "数据库未配置：jdbc_db");
        Assert.notNull(JDBC_USERNAME, "数据库未配置：jdbc_username");
        Assert.notNull(JDBC_PASSWORD, "数据库未配置：jdbc_password");
        Assert.notNull(JDBC_DIVER_CLASS_NAME, "数据库未配置：jdbc_driver");

        JDBCConnectionConfiguration jdbcConnectionConfiguration = new JDBCConnectionConfiguration();
        jdbcConnectionConfiguration.setConnectionURL(JDBC_URL);
        jdbcConnectionConfiguration.setUserId(JDBC_USERNAME);
        jdbcConnectionConfiguration.setPassword(JDBC_PASSWORD);
        jdbcConnectionConfiguration.setDriverClass(JDBC_DIVER_CLASS_NAME);

        for (TableModelParam tableParam : tableParams) {
            genCodeByCustomModelName(tableParam, jdbcConnectionConfiguration);
        }
    }

    /**
     * 通过数据表名称，和自定义的 Model 名称生成代码
     * 如输入表名称 "t_user_detail" 和自定义的 Model 名称 "User" 将生成 User、UserMapper、UserService ...
     * @param tableModelParam 数据表名称
     */
    public void genCodeByCustomModelName(TableModelParam tableModelParam, JDBCConnectionConfiguration jdbcConnectionConfiguration) {
        String tableName = tableModelParam.getTableName();
        String modelName = tableModelParam.getModelName();
        String primaryKeyType = tableModelParam.getPrimaryKeyType();

        genModelAndMapper(tableName, modelName, tableModelParam.isWithSwagger(), tableModelParam.isBeanBuilderMode(), tableModelParam.getColumnOverrides(), tableModelParam.getIgnoredColumns(), tableModelParam.isGeneratedKey(), jdbcConnectionConfiguration);

		genService(tableName, modelName, primaryKeyType);
		genController(tableName, modelName, primaryKeyType, tableModelParam.getApiPrefix());

    }

    public void genModelAndMapper(String tableName, String modelName, boolean withSwagger, boolean beanBuilderMode, List<ColumnOverride> columnOverrides, List<IgnoredColumn> ignoredColumns, boolean isGeneratedKey, JDBCConnectionConfiguration jdbcConnectionConfiguration) {
        Context context = new Context(ModelType.FLAT);
        context.setId("mybatis");
        context.setTargetRuntime("MyBatis3Simple");
        context.addProperty(PropertyRegistry.CONTEXT_BEGINNING_DELIMITER, "`");
        context.addProperty(PropertyRegistry.CONTEXT_ENDING_DELIMITER, "`");

        context.setJdbcConnectionConfiguration(jdbcConnectionConfiguration);

        //添加 tk.mybatis MapperPlugin
        PluginConfiguration pluginConfiguration = new PluginConfiguration();
        pluginConfiguration.setConfigurationType("tk.mybatis.mapper.generator.MapperPlugin");
        pluginConfiguration.addProperty("mappers", MAPPER_INTERFACE_REFERENCE);
        context.addPluginConfiguration(pluginConfiguration);

        if (withSwagger) {
            //添加 Swagger2 注解plugin <!-- 自动为entity生成swagger2文档-->
            pluginConfiguration = new PluginConfiguration();
            pluginConfiguration.setConfigurationType("mybatis.generator.plugins.GeneratorSwagger2Doc");
            pluginConfiguration.addProperty("apiModelAnnotationPackage", "io.swagger.annotations.ApiModel");
            pluginConfiguration.addProperty("apiModelPropertyAnnotationPackage", "io.swagger.annotations.ApiModelProperty");
            context.addPluginConfiguration(pluginConfiguration);
        }

        if (beanBuilderMode) {
            //<!-- 扩展entity的set方法 返回当前this实例，方便链式调用-->
            pluginConfiguration = new PluginConfiguration();
            pluginConfiguration.setConfigurationType("mybatis.generator.plugins.ExtendEntitySetter");
            context.addPluginConfiguration(pluginConfiguration);
        }

        JavaModelGeneratorConfiguration javaModelGeneratorConfiguration = new JavaModelGeneratorConfiguration();
        javaModelGeneratorConfiguration.setTargetProject(PROJECT_PATH + TARGET_JAVA_PATH);
        javaModelGeneratorConfiguration.setTargetPackage(MODEL_PACKAGE);
        context.setJavaModelGeneratorConfiguration(javaModelGeneratorConfiguration);

        SqlMapGeneratorConfiguration sqlMapGeneratorConfiguration = new SqlMapGeneratorConfiguration();
        sqlMapGeneratorConfiguration.setTargetProject(PROJECT_PATH + RESOURCES_PATH);
        sqlMapGeneratorConfiguration.setTargetPackage("Mapping");
        context.setSqlMapGeneratorConfiguration(sqlMapGeneratorConfiguration);

        JavaClientGeneratorConfiguration javaClientGeneratorConfiguration = new JavaClientGeneratorConfiguration();
        javaClientGeneratorConfiguration.setTargetProject(PROJECT_PATH + TARGET_JAVA_PATH);
        javaClientGeneratorConfiguration.setTargetPackage(MAPPER_PACKAGE);
        javaClientGeneratorConfiguration.setConfigurationType("XMLMAPPER");
        context.setJavaClientGeneratorConfiguration(javaClientGeneratorConfiguration);

        TableConfiguration tableConfiguration = new TableConfiguration(context);
        tableConfiguration.setTableName(tableName);

        if (!CollectionUtils.isEmpty(columnOverrides)) {
            columnOverrides.forEach(columnOverride -> tableConfiguration.addColumnOverride(columnOverride));
        }

        if (!CollectionUtils.isEmpty(ignoredColumns)) {
            ignoredColumns.forEach(ignoredColumn -> tableConfiguration.addIgnoredColumn(ignoredColumn));
        }
        if (!StringUtils.isEmpty(modelName))tableConfiguration.setDomainObjectName(modelName);

        if (isGeneratedKey) {
            tableConfiguration.setGeneratedKey(new GeneratedKey("id", "Mysql", true, null));
        }
        context.addTableConfiguration(tableConfiguration);

        List<String> warnings;
        MyBatisGenerator generator;
        try {
            Configuration config = new Configuration();
            config.addContext(context);
            config.validate();

            boolean overwrite = true;
            DefaultShellCallback callback = new DefaultShellCallback(overwrite);
            warnings = new ArrayList<String>();
            generator = new MyBatisGenerator(config, callback, warnings);
            generator.generate(null);
        } catch (Exception e) {
            throw new RuntimeException("生成Model和Mapper失败", e);
        }

        if (generator.getGeneratedJavaFiles().isEmpty() || generator.getGeneratedXmlFiles().isEmpty()) {
            throw new RuntimeException("生成Model和Mapper失败：" + warnings);
        }
        if (StringUtils.isEmpty(modelName)) modelName = tableNameConvertUpperCamel(tableName);
        System.out.println(modelName + ".java 生成成功");
        System.out.println(modelName + "Mapper.java 生成成功");
        System.out.println(modelName + "Mapper.xml 生成成功");
    }

    public void genService(String tableName, String modelName, String primaryKeyType) {
        try {
            freemarker.template.Configuration cfg = getConfiguration();

            Map<String, Object> data = new HashMap<>();
            data.put("date", DATE);
            data.put("author", AUTHOR);
            data.put("copyright", COPYRIGHT);
            String modelNameUpperCamel = StringUtils.isEmpty(modelName) ? tableNameConvertUpperCamel(tableName) : modelName;
            data.put("modelNameUpperCamel", modelNameUpperCamel);
            data.put("primaryKeyType", primaryKeyType);
            data.put("modelNameLowerCamel", tableNameConvertLowerCamel(tableName));
            data.put("basePackage", BASE_PACKAGE);

            File file = new File(PROJECT_PATH + TARGET_JAVA_PATH + PACKAGE_PATH_SERVICE + modelNameUpperCamel + "Service.java");
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            cfg.getTemplate("service.ftl").process(data,
                    new FileWriter(file));
            System.out.println(modelNameUpperCamel + "Service.java 生成成功");

            File file1 = new File(PROJECT_PATH + TARGET_JAVA_PATH + PACKAGE_PATH_SERVICE_IMPL + modelNameUpperCamel + "ServiceImpl.java");
            if (!file1.getParentFile().exists()) {
                file1.getParentFile().mkdirs();
            }
            cfg.getTemplate("service-impl.ftl").process(data,
                    new FileWriter(file1));
            System.out.println(modelNameUpperCamel + "ServiceImpl.java 生成成功");
        } catch (Exception e) {
            throw new RuntimeException("生成Service失败", e);
        }
    }

    public void genController(String tableName, String modelName, String primaryKeyType, String apiPrefix) {

        String modelNameUpperCamel = StringUtils.isEmpty(modelName) ? tableNameConvertUpperCamel(tableName) : modelName;
        File file = new File(PROJECT_PATH + TARGET_JAVA_PATH + PACKAGE_PATH_CONTROLLER + modelNameUpperCamel + "Controller.java");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        if(file.exists()) {
            //如果已存在controller，则放弃覆盖
            return;
        }

        try {
            freemarker.template.Configuration cfg = getConfiguration();
            if (apiPrefix==null) {
                apiPrefix = "";
            }

            Map<String, Object> data = new HashMap<>();
            data.put("date", DATE);
            data.put("author", AUTHOR);
            data.put("copyright", COPYRIGHT);
            data.put("baseRequestMapping", apiPrefix+modelNameConvertMappingPath(modelNameUpperCamel));
            data.put("modelNameUpperCamel", modelNameUpperCamel);
            data.put("primaryKeyType", primaryKeyType);
            data.put("modelNameLowerCamel", CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, modelNameUpperCamel));
            data.put("basePackage", BASE_PACKAGE);
            data.put("currentPackage", CONTROLLER_PACKAGE);

            //cfg.getTemplate("controller-restful.ftl").process(data, new FileWriter(file));
            cfg.getTemplate("controller.ftl").process(data, new FileWriter(file));

            System.out.println(modelNameUpperCamel + "Controller.java 生成成功");
        } catch (Exception e) {
            throw new RuntimeException("生成Controller失败", e);
        }
    }

    private freemarker.template.Configuration getConfiguration() throws IOException {
        freemarker.template.Configuration cfg = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_23);
        cfg.setDirectoryForTemplateLoading(new File(TEMPLATE_FILE_PATH));
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
        return cfg;
    }

    private String tableNameConvertLowerCamel(String tableName) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, tableName.toLowerCase());
    }

    private String tableNameConvertUpperCamel(String tableName) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, tableName.toLowerCase());
    }

    private String modelNameConvertMappingPath(String modelName) {
        String tableName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, modelName);
        return tableNameConvertMappingPath(tableName);
    }

    private String tableNameConvertMappingPath(String tableName) {
        tableName = tableName.toLowerCase();//兼容使用大写的表名
        String[] splitedTableNames = tableName.split("_");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < splitedTableNames.length; i++) {
            sb.append("/").append(Inflector.getInstance().pluralize(splitedTableNames[i]));
        }
        return sb.toString();
    }

    private String packageConvertPath(String packageName) {
        return String.format("/%s/", packageName.contains(".") ? packageName.replaceAll("\\.", "/") : packageName);
    }

}