package com.nt.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages={"com.nt.*"})
public class Start {

    private static Logger log = LoggerFactory.getLogger(Start.class);
    public static void main(String[] args) {
        SpringApplication.run(Start.class, args);
        log.info("服务已启动！！！");
    }
}
