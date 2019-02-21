package web.monitoring.monitor.service;

import web.monitoring.dbmanager.model.WebSiteForMonitoring;
import web.monitoring.monitor.MonitorManager;
import web.monitoring.monitor.config.ThreadConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class MonitoringService {

    private static final Logger logger = LoggerFactory.getLogger(MonitoringService.class);
    private Map<String, ScheduledFuture> taskMap = new HashMap<>();

    private MonitorManager monitorManager;
    private ApplicationContext context;
    private ThreadPoolTaskScheduler taskExecutor;

    public void beginMonitoring(List<WebSiteForMonitoring> websites, MonitorManager monitorManager){
        this.context = new AnnotationConfigApplicationContext(ThreadConfig.class);
        this.taskExecutor = (ThreadPoolTaskScheduler) context.getBean("taskExecutor");
        this.monitorManager = monitorManager;

        if(websites.size()>0){
            websites.stream().forEach(website -> monitor(website));
            logger.info("Monitoring has started..");
        } else {
            logger.info("No Web Sites to monitor.");
        }
    }

    @Async
    public void stopMonitoring(){
        taskMap.entrySet().stream().forEach(future -> {
            future.getValue().cancel(false);
        });
        taskMap.clear();
    }

    @Async
    public void monitor(WebSiteForMonitoring website){
        ScheduledFuture f = taskMap.get(website.getUrl());
        if(f==null){
            ScheduledFuture future = taskExecutor.scheduleAtFixedRate(
                new MonitoringTask(website, monitorManager),
                website.getMonitorFrequency()
            );
            taskMap.put(website.getUrl(), future);
        } else {
            if(!f.isDone() || !f.isCancelled()){
                f.cancel(false);
            }
            taskMap.remove(website.getUrl());
            monitor(website);
        }
    }

    @Async
    public void startMonitoringWebSite(WebSiteForMonitoring website){
        if(!taskMap.isEmpty()){
            monitor(website);
        }
    }

    @Async
    public void stopMonitoringWebSite(WebSiteForMonitoring website){
        ScheduledFuture future = taskMap.get(website.getUrl());
        if(future != null){
            if(!future.isDone() || !future.isCancelled()){
                future.cancel(false);
            }
            taskMap.remove(website.getUrl());
            logger.info("Stopped monitoring website : "+website.getName());
        }
    }
}
