package web.monitoring.monitor.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.scheduling.annotation.Async;
import web.monitoring.dbmanager.model.WebSiteForMonitoring;
import web.monitoring.monitor.MonitorManager;
import web.monitoring.monitor.config.ThreadConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class MonitoringService {

    private static final Logger logger = LoggerFactory.getLogger(MonitoringService.class);
    private Map<String, ScheduledFuture> taskMap = new HashMap<>();

    private ApplicationContext context;
    private MonitorManager monitorManager;
    private ThreadPoolTaskScheduler taskExecutor;

    public void beginMonitoring(List<WebSiteForMonitoring> websites, MonitorManager monitorManager){
        this.monitorManager = monitorManager;
        context = new AnnotationConfigApplicationContext(ThreadConfig.class);
        taskExecutor = (ThreadPoolTaskScheduler) context.getBean("taskExecutor");

        if(websites.size()>0){
            websites.stream().forEach(website -> monitorWebSite(website));
        }
        logger.info("Monitoring has started..");
    }

    @Async
    public void monitorWebSite(WebSiteForMonitoring website){
        ScheduledFuture f = taskMap.get(website.getUrl());
        if(f==null){
            ScheduledFuture future = taskExecutor.scheduleAtFixedRate(new MonitoringTask(website, monitorManager), website.getMonitorFrequency());
            taskMap.put(website.getUrl(), future);
        } else {
            if(f.cancel(true)){
                taskMap.remove(website.getUrl());
                monitorWebSite(website);
            }
        }
    }

    @Async
    public void stopMonitoringWebSite(WebSiteForMonitoring website){
        ScheduledFuture future = taskMap.get(website.getUrl());
        if(future != null){
            boolean cancel = future.cancel(true);
            if(cancel){
                taskMap.remove(website.getUrl());
                logger.info("Stopped monitoring website : "+website.getName());
            }
        }
    }
}
