package com.nt.controller.Config;


import com.alibaba.druid.pool.DruidDataSource;
import lombok.Data;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * 配置mysql数据源
 */
@Component
@Configuration
@ConfigurationProperties(prefix = "spring.datasource.mysql")
@MapperScan(basePackages = {"com.nt.**.mapper"}, sqlSessionFactoryRef = "mySqlSqlSessionFactory")
@Data
public class MysqlDataSourceConfig {
    @Value("${spring.datasource.mysql.jdbc-url}")
    private String dbUrl;
    @Value("${spring.datasource.mysql.username}")
    private String username;
    @Value("${spring.datasource.mysql.password}")
    private String password;
//    @Value("${spring.datasource.mysql.driverClassName}")
//    private String driverClassName;
//    @Value("${spring.datasource.mysql.initialSize}")
//    private int initialSize;
//    @Value("${spring.datasource.mysql.minIdle}")
//    private int minIdle;
//    @Value("${spring.datasource.mysql.maxActive}")
//    private int maxActive;
//    @Value("${spring.datasource.mysql.maxWait}")
//    private int maxWait;
//    @Value("${spring.datasource.mysql.timeBetweenEvictionRunsMillis}")
//    private int timeBetweenEvictionRunsMillis;
//    @Value("${spring.datasource.mysql.minEvictableIdleTimeMillis}")
//    private int minEvictableIdleTimeMillis;
//    @Value("${spring.datasource.mysql.validationQuery}")
//    private String validationQuery;
//    @Value("${spring.datasource.mysql.testWhileIdle}")
//    private boolean testWhileIdle;
//    @Value("${spring.datasource.mysql.testOnBorrow}")
//    private boolean testOnBorrow;
//    @Value("${spring.datasource.mysql.testOnReturn}")
//    private boolean testOnReturn;
//    @Value("${spring.datasource.mysql.poolPreparedStatements}")
//    private boolean poolPreparedStatements;
//    @Value("${spring.datasource.mysql.maxPoolPreparedStatementPerConnectionSize}")
//    private int maxPoolPreparedStatementPerConnectionSize;
//    @Value("${spring.datasource.mysql.filters}")
//    private String filters;
//    @Value("${spring.datasource.mysql.connectionProperties}")
//    private String connectionProperties;

    @Primary
    @Bean(name = "mySqlDataSource")
    //下面的注解作用就是从application.properties中读取以这个字符串开头的那些配置，设置为数据源的配置

    public DataSource testDataSource() {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl(dbUrl);
        druidDataSource.setUsername(username);
        druidDataSource.setPassword(password);
//        druidDataSource.setDriverClassName(driverClassName);
//
//        //configuration
//        druidDataSource.setRemoveAbandoned(true);
//        druidDataSource.setRemoveAbandonedTimeout(100000);
//        druidDataSource.setLogAbandoned(true);
//        druidDataSource.setInitialSize(initialSize);
//        druidDataSource.setMinIdle(minIdle);
//        druidDataSource.setMaxActive(maxActive);
//        druidDataSource.setMaxWait(maxWait);
//        druidDataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
//        druidDataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
//        druidDataSource.setValidationQuery(validationQuery);
//        druidDataSource.setTestWhileIdle(testWhileIdle);
//        druidDataSource.setTestOnBorrow(testOnBorrow);
//        druidDataSource.setTestOnReturn(testOnReturn);
//        druidDataSource.setPoolPreparedStatements(poolPreparedStatements);
//        druidDataSource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
        return druidDataSource;
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
