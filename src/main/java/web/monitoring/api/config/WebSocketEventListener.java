package web.monitoring.api.config;

import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import web.monitoring.monitor.MonitorManager;

@Component
public class WebSocketEventListener {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

    @Autowired
    private MonitorManager monitorManager;
    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        logger.info("Received a new web socket connection");
        /*

            Connection established : Starting up the monitoring of websites

         */
        monitorManager.initMonitoring();
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        logger.info("Connection has closed");
//      StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
//      String websiteName = (String) headerAccessor.getSessionAttributes().get("websiteName");
    }
}
