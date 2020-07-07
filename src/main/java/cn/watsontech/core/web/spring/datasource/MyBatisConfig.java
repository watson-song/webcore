package cn.watsontech.core.web.spring.datasource;

import cn.watsontech.core.mybatis.handler.MySqlJSONArrayTypeHandler;
import cn.watsontech.core.mybatis.handler.MySqlJSONObjectTypeHandler;
import cn.watsontech.core.mybatis.handler.MySqlJSONTypeHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Map;
import java.util.Properties;

/**
 * Copyright to watsontech
 * Created by Watson on 2019/12/24.
 */
@EnableTransactionManagement(order = 2)
@Configuration
@ConfigurationProperties(prefix = "mybatis.configuration")
public class MyBatisConfig {

    @Resource(name = "myRoutingDataSource")
    private DataSource myRoutingDataSource;

    private String entityTypeAliasesPackage;

    private Map<String, String> pageHelper;

    public String getEntityTypeAliasesPackage() {
        return entityTypeAliasesPackage;
    }

    public void setEntityTypeAliasesPackage(String entityTypeAliasesPackage) {
        this.entityTypeAliasesPackage = entityTypeAliasesPackage;
    }

    public Map<String, String> getPageHelper() {
        return pageHelper;
    }

    public void setPageHelper(Map<String, String> pageHelper) {
        this.pageHelper = pageHelper;
    }

    @Bean
//    @ConfigurationProperties(prefix = "mybatis.configuration")
    public org.apache.ibatis.session.Configuration globalConfiguration() {
        org.apache.ibatis.session.Configuration sessionConfiguration =  new org.apache.ibatis.session.Configuration();
        sessionConfiguration.getTypeHandlerRegistry().register(MySqlJSONArrayTypeHandler.class);
        sessionConfiguration.getTypeHandlerRegistry().register(MySqlJSONTypeHandler.class);
        sessionConfiguration.getTypeHandlerRegistry().register(MySqlJSONObjectTypeHandler.class);
        sessionConfiguration.setMapUnderscoreToCamelCase(true);
        sessionConfiguration.setCallSettersOnNulls(true);
        sessionConfiguration.setJdbcTypeForNull(JdbcType.NULL);
        return sessionConfiguration;
    }

    @Bean
    @Primary
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(myRoutingDataSource);
        sqlSessionFactoryBean.setTypeAliasesPackage(entityTypeAliasesPackage);
        sqlSessionFactoryBean.setConfiguration(globalConfiguration());
        sqlSessionFactoryBean.setPlugins(factoryPlugins());
        return sqlSessionFactoryBean.getObject();
    }

    private Interceptor[] factoryPlugins() {
        Interceptor pageInterceptor = new com.github.pagehelper.PageInterceptor();
        Properties properties = new Properties();
        if (pageHelper!=null) {
            for (String key:pageHelper.keySet()) {
                properties.put(key, pageHelper.get(key));
            }
        }
        pageInterceptor.setProperties(properties);
        return new Interceptor[]{pageInterceptor};
    }

    @Bean
    public PlatformTransactionManager platformTransactionManager() {
        return new DataSourceTransactionManager(myRoutingDataSource);
    }
}