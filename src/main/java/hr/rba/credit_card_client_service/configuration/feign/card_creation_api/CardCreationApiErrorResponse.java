package hr.rba.credit_card_client_service.configuration.feign.card_creation_api;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CardCreationApiErrorResponse {
  String code;
  String id;
  String description;
}
