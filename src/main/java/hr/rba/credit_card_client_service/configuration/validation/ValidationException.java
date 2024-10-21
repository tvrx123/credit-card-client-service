package hr.rba.credit_card_client_service.configuration.validation;

public class ValidationException extends RuntimeException {
  public ValidationException(String message) {
    super(message);
  }
}
