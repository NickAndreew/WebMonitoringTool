package web.monitoring.dbmanager.repositories;

import web.monitoring.dbmanager.model.WebSiteCheckResult;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WebSiteCheckResultRepository extends JpaRepository<WebSiteCheckResult, Long> {
    WebSiteCheckResult findById(long id);
    List<WebSiteCheckResult> findByName(String name);
}
