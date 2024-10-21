package hr.rba.credit_card_client_service.configuration.audit;

import java.util.Optional;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
class JpaAuditingConfiguration implements AuditorAware<String> {

  @Override
  public Optional<String> getCurrentAuditor() {
    // In an applidcation with working security, user will be pulled from security context
    return Optional.of("RBA_EMPLOYEE");
  }
}
