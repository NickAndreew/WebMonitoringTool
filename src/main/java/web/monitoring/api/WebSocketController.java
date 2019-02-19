package web.monitoring.api;

import web.monitoring.api.models.responses.ResponseMessage;
import web.monitoring.api.models.requests.RequestMessage;
import web.monitoring.dbmanager.model.WebSiteCheckResult;
import web.monitoring.monitor.MonitorManager;
import web.monitoring.dbmanager.DBManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
////////////////////////////////////////////////////////////////////////////     TEST
    @MessageMapping("/getSomeResponse")
    @SendTo("/monitor/public")
    public ResponseMessage getSomeResponse(@Payload RequestMessage payload) {
        ResponseMessage message = new ResponseMessage();

        // API TEST METHOD.....................................
         dbManager.testDbAddWebSite();
        logger.info("The message of TYPE '"+payload.getType()+"', with the following context '"+payload.getContext()+"' received.");
        payload.setContext(dbManager.getMessageCheck());
        return message;
    }
/////////////////////////////////////////////////////////////////////////////     TEST
    @MessageMapping("/addWebSite")
    @SendTo("/monitor/public")
    public ResponseMessage addWebSite(@Payload RequestMessage payload) {
        ResponseMessage message = new ResponseMessage();
        if(payload.getWebSiteForMonitoring()!=null){
            dbManager.saveNewWebSiteForMonitoring(payload.getWebSiteForMonitoring());
            monitorManager.addToMonitor(payload.getWebSiteForMonitoring());

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

    @MessageMapping("/pauseMonitoring")
    @SendTo("/monitor/public")
    public ResponseMessage pauseMonitoring(@Payload RequestMessage payload) {
        ResponseMessage message = new ResponseMessage();
        if (payload.getWebSiteForMonitoring()!=null) {
            monitorManager.stopMonitoring(payload.getWebSiteForMonitoring());

            message.setType(ResponseMessage.MessageType.MONITOR_PAUSED);
            message.setWebSiteForMonitoring(payload.getWebSiteForMonitoring());
        } else {
            message.setType(ResponseMessage.MessageType.ERROR);
            message.setContext("Input payload did not contain website, please try again!");
        }
        return message;
    }

    @MessageMapping("/continueMonitoring")
    @SendTo("/monitor/public")
    public ResponseMessage continueMonitoring(@Payload RequestMessage payload) {
        ResponseMessage message = new ResponseMessage();
        if(payload.getWebSiteForMonitoring()!=null){
            monitorManager.addToMonitor(payload.getWebSiteForMonitoring());

            message.setType(ResponseMessage.MessageType.MONITOR_CONTINUED);
            message.setWebSiteForMonitoring(payload.getWebSiteForMonitoring());
        } else {
            message.setType(ResponseMessage.MessageType.ERROR);
            message.setContext("Input payload did not contain website, please try again!");
        }
        return message;
    }

    @MessageMapping("/getWebSitesForMonitoring")
    @SendTo("/monitor/public")
    public ResponseMessage getWebSitesForMonitoring() {
        ResponseMessage message = new ResponseMessage();
        message.setType(ResponseMessage.MessageType.ALL_WEBSITES);
        message.setWebSitesForMonitoring(dbManager.getWebSitesForMonitoring());
        return message;
    }

    @MessageMapping("/getWebSitesChecksResults")
    @SendTo("/monitor/public")
    public ResponseMessage getWebSitesChecksResults() {
        ResponseMessage message = new ResponseMessage();
        message.setType(ResponseMessage.MessageType.ALL_CHECK_RESULT);
        message.setWebSitesChecksResults(dbManager.getWebSitesCheckResults());
        return message;
    }

    @SendTo("/monitor/public")
    public static ResponseMessage sendNewCheckResult(WebSiteCheckResult website){
        ResponseMessage message = new ResponseMessage();
        if(website!=null){
            message.setType(ResponseMessage.MessageType.NEW_CHECK_RESULT);
            message.setWebSiteCheckResult(website);
        } else {
            message.setType(ResponseMessage.MessageType.ERROR);
            message.setContext("No website check result");
        }
        return message;
    }
}
