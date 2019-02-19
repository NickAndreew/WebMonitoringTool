package web.monitoring.dbmanager.repositories;

import web.monitoring.dbmanager.model.WebSiteForMonitoring;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WebSiteForMonitoringRepository extends JpaRepository<WebSiteForMonitoring, Long> {
    WebSiteForMonitoring findByUrl(String url);
}
