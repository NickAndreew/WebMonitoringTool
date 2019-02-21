package web.monitoring.dbmanager;

import java.util.List;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import web.monitoring.dbmanager.model.WebSiteCheckResult;
import web.monitoring.dbmanager.model.WebSiteForMonitoring;
import web.monitoring.dbmanager.repositories.WebSiteCheckResultRepository;
import web.monitoring.dbmanager.repositories.WebSiteForMonitoringRepository;

import org.springframework.stereotype.Service;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import lombok.NoArgsConstructor;

@Service
@NoArgsConstructor
public class DBManager {

    private static final Logger logger = LoggerFactory.getLogger(DBManager.class);

    @Autowired
    private WebSiteForMonitoringRepository webSiteForMonitoringRepository;
    @Autowired
    private WebSiteCheckResultRepository checkResultsRepository;
    @Autowired
    private SimpMessagingTemplate template;

    @Transactional
    public void saveNewWebSiteForMonitoring(WebSiteForMonitoring website){
        if(website!=null){
            webSiteForMonitoringRepository.save(website);
        }
    }

    @Transactional
    public void saveNewCheckResult(WebSiteCheckResult result){
        if(result!=null){
            checkResultsRepository.save(result);
        }
    }

    @Transactional
    public void deleteWebSiteForMonitoring(WebSiteForMonitoring website){
        WebSiteForMonitoring site = webSiteForMonitoringRepository.findByUrl(website.getUrl());
        if(site!=null){
            webSiteForMonitoringRepository.delete(site);
        } else {
            logger.error("Couldn't find the website with the specified URL: "+website.getUrl()+" .");
        }
    }

    public List<WebSiteForMonitoring> getWebSitesForMonitoring(){
        List<WebSiteForMonitoring> websites = webSiteForMonitoringRepository.findAll();
        if(websites.size()>0){
            return websites;
        } else {
            return new ArrayList<>();
        }
    }

    public List<WebSiteCheckResult> getWebSitesCheckResults() {
        return checkResultsRepository.findAll();
    }
}
