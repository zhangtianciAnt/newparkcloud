package com.nt.controller.Config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;


/**
 * 配置sqlServer数据源
 */
@Configuration
@MapperScan(basePackages = {"com.nt.service_BASFSQL.mapper"}, sqlSessionFactoryRef = "sqlServerSqlSessionFactory")
public class SqlServerDataSourceConfig {

    @Bean(name = "sqlServerDataSource")
    //下面的注解作用就是从application.properties中读取以这个字符串开头的那些配置，设置为数据源的配置
    @ConfigurationProperties(prefix = "spring.datasource.sqlserver")
    public DataSource testDataSource() {
        return DataSourceBuilder.create().build();
    }


    @Bean(name = "sqlServerSqlSessionFactory")
    public SqlSessionFactory testSqlSessionFactory(@Qualifier("sqlServerDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/*.xml"));
        return bean.getObject();
    }


    @Bean(name = "sqlServerTransactionManager")
    public DataSourceTransactionManager testTransactionManager(@Qualifier("sqlServerDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }


    @Bean(name = "sqlServerSqlSessionTemplate")
    public SqlSessionTemplate testSqlSessionTemplate(@Qualifier("sqlServerSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}

