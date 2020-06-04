package cn.watsontech.core.web.spring.datasource;

import cn.watsontech.core.mybatis.handler.MySqlJSONArrayTypeHandler;
import cn.watsontech.core.mybatis.handler.MySqlJSONObjectTypeHandler;
import cn.watsontech.core.mybatis.handler.MySqlJSONTypeHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
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
import java.util.Properties;

/**
 * Copyright to watsontech
 * Created by Watson on 2019/12/24.
 */
@EnableTransactionManagement(order = 2)
@Configuration
public class MyBatisConfig {

    @Resource(name = "myRoutingDataSource")
    private DataSource myRoutingDataSource;
    // sqlSessionFactoryBean.setTypeAliasesPackage(entityTypeAliasesPackage); com.***.entity
    @Value("${mybatis.configuration.entityTypeAliasesPackage}")
    private String entityTypeAliasesPackage;

    @Value("${mybatis.configuration.pageHelper}")
    private Properties pageHelperProperties;

    @Bean
    @ConfigurationProperties(prefix = "mybatis.configuration")
    public org.apache.ibatis.session.Configuration globalConfiguration() {
        org.apache.ibatis.session.Configuration sessionConfiguration =  new org.apache.ibatis.session.Configuration();
        sessionConfiguration.getTypeHandlerRegistry().register(MySqlJSONArrayTypeHandler.class);
        sessionConfiguration.getTypeHandlerRegistry().register(MySqlJSONTypeHandler.class);
        sessionConfiguration.getTypeHandlerRegistry().register(MySqlJSONObjectTypeHandler.class);
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
        pageInterceptor.setProperties(pageHelperProperties);
        return new Interceptor[]{pageInterceptor};
    }

    @Bean
    public PlatformTransactionManager platformTransactionManager() {
        return new DataSourceTransactionManager(myRoutingDataSource);
    }
}