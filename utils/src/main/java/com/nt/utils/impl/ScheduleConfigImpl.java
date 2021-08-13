package com.nt.utils.impl;

import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executor;

@Configuration
@EnableScheduling
public class ScheduleConfigImpl implements SchedulingConfigurer {
    @Value("${scheduling.enabled}")
    private boolean taskSwitch;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        if(!taskSwitch){
            //清空扫描到的定时任务即可
            taskRegistrar.setTriggerTasks(Maps.newHashMap());
            taskRegistrar.setCronTasks(Maps.newHashMap());
            taskRegistrar.setFixedRateTasks(Maps.newHashMap());
            taskRegistrar.setFixedDelayTasks(Maps.newHashMap());
        }
        taskRegistrar.setScheduler(taskExecutor());
    }

    @Bean(destroyMethod = "shutdown")
    public Executor taskExecutor() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(10);
        taskScheduler.initialize();
        return taskScheduler;
    }

}
