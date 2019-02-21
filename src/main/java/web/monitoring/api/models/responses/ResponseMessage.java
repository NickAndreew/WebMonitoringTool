package web.monitoring.api.models.responses;

import web.monitoring.dbmanager.model.WebSiteCheckResult;
import web.monitoring.dbmanager.model.WebSiteForMonitoring;

import org.springframework.lang.NonNull;

import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@ToString
@NoArgsConstructor
public class ResponseMessage {

    public enum MessageType {
        ALL_RESULTS_AND_WEBSITES, ALL_WEBSITES,
        WEBSITE_ADDED, WEBSITE_REMOVED,
        MONITORING_STOPPED, MONITORING_STARTED,
        NEW_CHECK_RESULT, ERROR
    }

    @NonNull
    private MessageType type;
    private String context;
    private WebSiteForMonitoring webSiteForMonitoring;
    private WebSiteCheckResult webSiteCheckResult;
    private List<WebSiteCheckResult> webSitesChecksResults;
    private List<WebSiteForMonitoring> webSitesForMonitoring;

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

    public List<WebSiteCheckResult> getWebSitesChecksResults() {
        return webSitesChecksResults;
    }

    public void setWebSitesChecksResults(List<WebSiteCheckResult> webSitesChecksResults) {
        this.webSitesChecksResults = webSitesChecksResults;
    }

    public List<WebSiteForMonitoring> getWebSitesForMonitoring() {
        return webSitesForMonitoring;
    }

    public void setWebSitesForMonitoring(List<WebSiteForMonitoring> webSitesForMonitoring) {
        this.webSitesForMonitoring = webSitesForMonitoring;
    }

    public WebSiteCheckResult getWebSiteCheckResult() {
        return webSiteCheckResult;
    }

    public void setWebSiteCheckResult(WebSiteCheckResult webSiteCheckResult) {
        this.webSiteCheckResult = webSiteCheckResult;
    }
}
