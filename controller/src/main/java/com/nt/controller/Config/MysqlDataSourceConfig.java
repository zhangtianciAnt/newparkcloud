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
 * 配置mysql数据源
 */
@Configuration
@MapperScan(basePackages = {"com.nt.**.mapper"}, sqlSessionFactoryRef = "mySqlSqlSessionFactory")
public class MysqlDataSourceConfig {

    @Primary
    @Bean(name = "mySqlDataSource")
    //下面的注解作用就是从application.properties中读取以这个字符串开头的那些配置，设置为数据源的配置
    @ConfigurationProperties(prefix = "spring.datasource.mysql")
    public DataSource testDataSource() {
        return DataSourceBuilder.create().build();
    }
    @Primary
    @Bean(name = "mySqlSqlSessionFactory")
    public SqlSessionFactory testSqlSessionFactory(@Qualifier("mySqlDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/*.xml"));
        return bean.getObject();
    }
    @Primary
    @Bean(name = "mySqlTransactionManager")
    public DataSourceTransactionManager testTransactionManager(@Qualifier("mySqlDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
    @Primary
    @Bean(name = "mySqlSqlSessionTemplate")
    public SqlSessionTemplate testSqlSessionTemplate(@Qualifier("mySqlSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
