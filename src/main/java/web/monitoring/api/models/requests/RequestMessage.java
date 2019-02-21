package web.monitoring.api.models.requests;

import web.monitoring.dbmanager.model.WebSiteForMonitoring;

import lombok.NoArgsConstructor;
import lombok.ToString;

import org.springframework.lang.NonNull;

@ToString
@NoArgsConstructor
public class RequestMessage {

    public enum MessageType {
        CONNECTED, ADD_WEBSITE,
        REMOVE_WEBSITE, PAUSE_MONITOR, CONTINUE_MONITOR
    }

    @NonNull
    private MessageType type;
    private String context;
    private WebSiteForMonitoring webSiteForMonitoring;

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public WebSiteForMonitoring getWebSiteForMonitoring() {
        return webSiteForMonitoring;
    }

    public void setWebSiteForMonitoring(WebSiteForMonitoring webSiteForMonitoring) {
        this.webSiteForMonitoring = webSiteForMonitoring;
    }
}
