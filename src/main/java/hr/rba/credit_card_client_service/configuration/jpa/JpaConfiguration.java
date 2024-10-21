package hr.rba.credit_card_client_service.configuration.jpa;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = {"hr.rba.credit_card_client_service.feature"})
public class JpaConfiguration {}
