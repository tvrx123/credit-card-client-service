package hr.rba.credit_card_client_service.configuration.feign.card_creation_api;

import lombok.Data;

@Data
public class CardCreationApiException extends RuntimeException {
  private int status;
  private CardCreationApiErrorResponse responseBody;

  public CardCreationApiException(int status, CardCreationApiErrorResponse responseBody) {
    super();
    this.status = status;
    this.responseBody = responseBody;
  }
}
