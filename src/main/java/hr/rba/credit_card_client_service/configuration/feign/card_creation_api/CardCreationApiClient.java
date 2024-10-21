package hr.rba.credit_card_client_service.configuration.feign.card_creation_api;

import hr.rba.credit_card_client_service.feature.card_application.CardApplicationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
    name = "card-creation-api",
    url = "http://api.something.com/v1, configuration = CardCreationApiFeignConfiguration.class")
public interface CardCreationApiClient {
  @PostMapping("/api/v1/card-request")
  void createNewCard(@RequestBody CardApplicationRequest request);
}
