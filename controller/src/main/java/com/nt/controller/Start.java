package com.nt.controller;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_objdetect;
import org.bytedeco.javacv.*;
import org.bytedeco.javacv.Frame;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.boot.web.servlet.FilterRegistrationBean;

import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.swing.*;
import java.awt.*;

import static com.nt.controller.DetectFace.Camera.recordCamera;

@SpringBootApplication
@ComponentScan(basePackages={"com.nt.*"})
@MapperScan(basePackages = "com.*.mapper")
@EnableTransactionManagement
public class Start {

    private static Logger log = LoggerFactory.getLogger(Start.class);
    public static void main(String[] args) throws Exception {
        SpringApplication.run(Start.class, args);
        log.info("服务已启动！！！");
//        System.setProperty("java.awt.headless", "false");
//        recordCamera("rtmp://59.80.34.104:1935/hls/test",25);

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


}
