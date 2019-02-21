package web.monitoring.monitor.service;

import web.monitoring.dbmanager.model.WebSiteCheckResult;
import web.monitoring.dbmanager.model.WebSiteForMonitoring;
import web.monitoring.monitor.MonitorManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;

public class MonitoringTask extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(MonitoringTask.class);
    private DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy'T'HH:MM:SS");

    private WebSiteForMonitoring website;
    private MonitorManager monitorManager;

    public MonitoringTask(WebSiteForMonitoring website, MonitorManager monitorManager) {
        this.website = website;
        this.monitorManager = monitorManager;
    }

    @Override
    public void run(){
        logger.info("Monitoring Task execution began..");
        String url = website.getUrl();
        Date date = new Date();

        long responseCode;
        long responseTime;
        long responseSize;

        WebSiteCheckResult result = new WebSiteCheckResult();
        result.setUrl(url);
        result.setName(website.getName());
        result.setCheckTime(dateFormat.format(date));
        result.setResponseStatus(WebSiteCheckResult.Status.CRITICAL);

        try {
            long t = System.currentTimeMillis();
            Response response = Jsoup.connect(url)
                    .method(Method.GET)
                    .timeout(5000)
                    .execute();
            responseTime = System.currentTimeMillis() - t;
            responseSize = response.headers().entrySet().size() + response.bodyAsBytes().length;
            responseCode = (long) response.statusCode();

            result.setResponseCode(responseCode);
            result.setResponseTime(responseTime);
            result.setResponseSize(responseSize);

            if (responseCode == 200 &&
                (responseTime>website.getDelayLowBound() &&
                    responseTime<website.getDelayUpBound()) &&
                        (responseSize>website.getSizeLowBound() &&
                            responseSize<website.getSizeUpBound())) {
                result.setResponseStatus(WebSiteCheckResult.Status.OK);
            } else if(responseCode == 200) {
                result.setResponseStatus(WebSiteCheckResult.Status.WARNING);
            }
        } catch (Exception e){
            logger.error(e.toString());
        }

        monitorManager.saveCheckResult(result);
        logger.info("Website : "+website.getName()+" have been checked.");
    }
}
