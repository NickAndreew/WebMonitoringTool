package web.monitoring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableAutoConfiguration
@EnableJpaRepositories(basePackages = "web.monitoring.dbmanager.repositories")
@ComponentScan("web.monitoring")
public class WebAgentAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebAgentAppApplication.class, args);
	}
}

