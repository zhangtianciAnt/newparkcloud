package com.nt.controller;

import com.mongodb.MongoClientOptions;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@SpringBootApplication
@ComponentScan(basePackages={"com.nt.*"})
@MapperScan(basePackages = "com.nt.**.mapper")
@EnableTransactionManagement
@ServletComponentScan("com.nt.*")
public class Start {

    private static Logger log = LoggerFactory.getLogger(Start.class);
    public static void main(String[] args) throws Exception {
        SpringApplication.run(Start.class, args);
        log.info("服务已启动！！！");
    }

    @Bean
    public FilterRegistrationBean corsFilter() {
      UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
      CorsConfiguration config = new CorsConfiguration();
      config.setAllowCredentials(true);
      config.addAllowedOrigin("*");
      config.addAllowedHeader("*");
      config.addAllowedMethod("*");
      source.registerCorsConfiguration("/**", config);
      FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
      bean.setOrder(0);
      return bean;
    }

    @Bean
    public MongoClientOptions mongoOptions() {
        return MongoClientOptions.builder()
                .maxConnectionIdleTime(60000)
                .maxConnectionLifeTime(0)
                .connectTimeout(10000)
                .socketTimeout(0)
                .maxWaitTime(120000)
                .build();
    }
}
