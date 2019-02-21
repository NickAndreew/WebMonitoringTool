package web.monitoring.monitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import web.monitoring.api.WebSocketController;
import web.monitoring.dbmanager.model.WebSiteCheckResult;
import web.monitoring.dbmanager.model.WebSiteForMonitoring;
import web.monitoring.dbmanager.DBManager;
import web.monitoring.monitor.service.MonitoringService;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class MonitorManager {

    private static final Logger logger = LoggerFactory.getLogger(MonitorManager.class);

    @Autowired
    private DBManager dbManager;
    @Autowired
    private WebSocketController webSocketController;
    @Autowired
    private MonitoringService monitoringService;

    public void initMonitoring(){
        /*
            initial monitoring
        */
        monitoringService.beginMonitoring(dbManager.getWebSitesForMonitoring(), this);
        logger.info("\n  initMonitoring() \n");
    }

    public void stopMonitoring(){
        /*
            Stops all monitoring processes in a pool
        */
        monitoringService.stopMonitoring();
    }

    public void addToMonitor(WebSiteForMonitoring website){
        /*
            adds website to monitoring pool
        */
        monitoringService.startMonitoringWebSite(website);
    }

    public void stopMonitoring(WebSiteForMonitoring website){
        /*
            stops monitoring website
        */
        monitoringService.stopMonitoringWebSite(website);
    }

    public void saveCheckResult(WebSiteCheckResult result){
        /*
            method is being used from MonitoringTask, saves result of every monitoring check and sends to client
        */
        dbManager.saveNewCheckResult(result);
        webSocketController.sendNewCheckResult(result);
        logger.info("Save Check Result for "+result.getName());
    }
}
