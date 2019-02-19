package web.monitoring.dbmanager.repositories;

import org.springframework.stereotype.Repository;
import web.monitoring.dbmanager.model.WebSiteCheckResult;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface WebSiteCheckResultRepository extends JpaRepository<WebSiteCheckResult, Long> {
    WebSiteCheckResult findById(long id);
    List<WebSiteCheckResult> findByName(String name);
}
