package com.smdb.spatialmeta.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@Configuration
@EnableAsync
public class AsyncThreadPool implements AsyncConfigurer {

    /**
     * 使用阿里的TTL封装线程池，子线程可以有效使用父线程的上下文
     * 替换默认的Executor实例-默认使用的线程池
     */
    @Primary
    @Override
    @Bean(name = "asyncExecutor")
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();

        // 默认值是1
        int cpuNum = Runtime.getRuntime().availableProcessors();

        //初始线程池大小
        //fixme 线程池情况根据需要自己调整
        taskExecutor.setCorePoolSize(1);
        // 最大线程池大小，默认值是Integer.MAX_VALUE
        taskExecutor.setMaxPoolSize(3);
        taskExecutor.setThreadNamePrefix("pool->");
        // 默认值是60s
        taskExecutor.setKeepAliveSeconds(90);
        // 如果超出最大线程池大小，会进入这里队列 默认值是Integer.MAX_VALUE
        taskExecutor.setQueueCapacity(5);
        // 如果超出队列大小，则直接在原线程执行 CallerRunsPolicy：不在新线程中执行任务，而是有调用者所在的线程来执行
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        taskExecutor.setAllowCoreThreadTimeOut(false);
        taskExecutor.initialize();

        log.info("自定义线程池初始化完成，核心线程数量：{}，最大线程数量：{}，队列容量：{}", cpuNum, 8 * cpuNum + 1, 5);
        return taskExecutor;
    }

    /**
     * 异步执行异常处理器
     */
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (throwable, method, args) -> log.error("线程池执行发生异常,error message:{}", throwable.getMessage());
    }

}
