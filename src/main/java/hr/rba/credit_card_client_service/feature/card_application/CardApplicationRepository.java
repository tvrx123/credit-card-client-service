package hr.rba.credit_card_client_service.feature.card_application;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardApplicationRepository extends JpaRepository<CardApplication, Long> {

    Optional<CardApplication> findByOib(String oib);

}
