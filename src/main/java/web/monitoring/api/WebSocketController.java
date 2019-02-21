package web.monitoring.api;

import web.monitoring.api.models.responses.ResponseMessage;
import web.monitoring.api.models.requests.RequestMessage;
import web.monitoring.dbmanager.model.WebSiteCheckResult;
import web.monitoring.dbmanager.model.WebSiteForMonitoring;
import web.monitoring.monitor.MonitorManager;
import web.monitoring.dbmanager.DBManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketController.class);

    @Autowired
    private DBManager dbManager;
    @Autowired
    private MonitorManager monitorManager;
    @Autowired
    private SimpMessagingTemplate template;

    @MessageMapping("/connected")
    @SendTo("/monitor/public")
    public ResponseMessage connected() {
        ResponseMessage message = new ResponseMessage();
        message.setType(ResponseMessage.MessageType.ALL_RESULTS_AND_WEBSITES);
        message.setWebSitesForMonitoring(dbManager.getWebSitesForMonitoring());
        message.setWebSitesChecksResults(dbManager.getWebSitesCheckResults());
        return message;
    }

    @MessageMapping("/startMonitoring")
    @SendTo("/monitor/public")
    public ResponseMessage startMonitoring() {
        monitorManager.initMonitoring();
        ResponseMessage message = new ResponseMessage();
        message.setType(ResponseMessage.MessageType.MONITORING_STARTED);
        message.setContext("Monitoring Started");
        return message;
    }

    @MessageMapping("/stopMonitoring")
    @SendTo("/monitor/public")
    public ResponseMessage stopMonitoring() {
        monitorManager.stopMonitoring();
        ResponseMessage message = new ResponseMessage();
        message.setType(ResponseMessage.MessageType.MONITORING_STOPPED);
        message.setContext("Monitoring Stopped");
        return message;
    }

    @MessageMapping("/addWebSite")
    @SendTo("/monitor/public")
    public ResponseMessage addWebSite(@Payload RequestMessage payload) {
        ResponseMessage message = new ResponseMessage();
        if(payload.getWebSiteForMonitoring()!=null){
            dbManager.saveNewWebSiteForMonitoring(payload.getWebSiteForMonitoring());
            monitorManager.addToMonitor(payload.getWebSiteForMonitoring());
            sendNewWebSite(payload.getWebSiteForMonitoring());

            message.setType(ResponseMessage.MessageType.WEBSITE_ADDED);
            message.setWebSiteForMonitoring(payload.getWebSiteForMonitoring());
        } else {
            message.setType(ResponseMessage.MessageType.ERROR);
            message.setContext("Input payload did not contain website, please try again!");
        }
        return message;
    }

    @MessageMapping("/deleteWebSite")
    @SendTo("/monitor/public")
    public ResponseMessage deleteWebSite(@Payload RequestMessage payload) {
        ResponseMessage message = new ResponseMessage();
        if (payload.getWebSiteForMonitoring()!=null) {
            dbManager.deleteWebSiteForMonitoring(payload.getWebSiteForMonitoring());
            monitorManager.stopMonitoring(payload.getWebSiteForMonitoring());

            message.setType(ResponseMessage.MessageType.WEBSITE_REMOVED);
            message.setWebSiteForMonitoring(payload.getWebSiteForMonitoring());
        } else {
            message.setType(ResponseMessage.MessageType.ERROR);
            message.setContext("Input payload did not contain website, please try again!");
        }
        return message;
    }

    @MessageMapping("/getAllWebSites")
    @SendTo("/monitor/public")
    public ResponseMessage getAllWebSites(){
        ResponseMessage message = new ResponseMessage();
        message.setType(ResponseMessage.MessageType.ALL_WEBSITES);
        message.setContext("All websites");
        message.setWebSitesForMonitoring(dbManager.getWebSitesForMonitoring());
        return message;
    }

    public void sendNewCheckResult(WebSiteCheckResult website){
        ResponseMessage message = new ResponseMessage();
        message.setType(ResponseMessage.MessageType.NEW_CHECK_RESULT);
        message.setWebSiteCheckResult(website);
        template.convertAndSend("/monitor/public", message);
    }

    public void sendNewWebSite(WebSiteForMonitoring website){
        ResponseMessage message = new ResponseMessage();
        message.setType(ResponseMessage.MessageType.WEBSITE_ADDED);
        message.setWebSiteForMonitoring(website);
        template.convertAndSend("/monitor/public", message);
    }
}
