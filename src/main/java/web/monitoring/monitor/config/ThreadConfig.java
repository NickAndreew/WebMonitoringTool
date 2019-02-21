package web.monitoring.monitor.config;

import web.monitoring.monitor.service.MonitoringService;

import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;

@Configuration
@ComponentScan(
    basePackages = "web.monitoring.monitor.service",
    basePackageClasses={MonitoringService.class}
)
public class ThreadConfig {
    @Bean
    public ThreadPoolTaskScheduler taskExecutor(){
        ThreadPoolTaskScheduler executor = new ThreadPoolTaskScheduler();
        executor.setPoolSize(10);
        executor.setThreadNamePrefix("ThreadPoolTaskScheduler");
        executor.setRemoveOnCancelPolicy(true);
        executor.initialize();
        return executor;
    }
}
