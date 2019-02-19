package web.monitoring.monitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import web.monitoring.dbmanager.model.WebSiteCheckResult;
import web.monitoring.dbmanager.model.WebSiteForMonitoring;
import web.monitoring.dbmanager.DBManager;
import web.monitoring.monitor.service.MonitoringService;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import static web.monitoring.api.WebSocketController.sendNewCheckResult;

@Service
public class MonitorManager {

    private static final Logger logger = LoggerFactory.getLogger(MonitorManager.class);

    @Autowired
    private DBManager dbManager;
    @Autowired
    private MonitoringService monitoringService;

    public void initMonitoring(){
        /*
            initial monitoring
        */
        monitoringService.beginMonitoring(dbManager.getWebSitesForMonitoring(), this);
        logger.info("\n  initMonitoring() \n");
    }

    public void addToMonitor(WebSiteForMonitoring website){
        /*
            adds website to monitoring pool
        */
        monitoringService.monitorWebSite(website);
    }

    public void stopMonitoring(WebSiteForMonitoring website){
        /*
            stops monitoring website
        */
        monitoringService.stopMonitoringWebSite(website);
    }

    public void saveCheckResult(WebSiteCheckResult result){
        /*
            method is being used from MonitoringTask, saves result of every monitoring check and send to client !!!
        */
        dbManager.saveNewCheckResult(result);
        sendNewCheckResult(result);
        logger.info("\n saveCheckResult \n");
    }
}
