package com.zqdinsane.schedulerutil.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
/**
 * 配置类
 * @author zhuangqingdian
 * @date 2021/5/14
 */
@Configuration
public class SchedulerConfig {
    @Bean
    public TaskScheduler taskScheduler(){
        ThreadPoolTaskScheduler threadPool = new ThreadPoolTaskScheduler();
        //核心线程数
        threadPool.setPoolSize(10);
        threadPool.setRemoveOnCancelPolicy(true);
        threadPool.setThreadNamePrefix("schedulerTask-");
        return threadPool;
    }
}
