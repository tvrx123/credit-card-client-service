package hr.rba.credit_card_client_service.configuration.validation;

import com.fasterxml.jackson.annotation.JsonProperty;
import hr.rba.credit_card_client_service.configuration.feign.card_creation_api.CardCreationApiErrorResponse;
import hr.rba.credit_card_client_service.configuration.feign.card_creation_api.CardCreationApiException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@ResponseBody
@RequiredArgsConstructor
public class ValidationControllerAdvice extends ResponseEntityExceptionHandler {

  private final MessageSource messageSource;

  @ExceptionHandler(ValidationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ValidationResponse resourceNotFoundException(ValidationException ex, WebRequest request) {
    String message =
        messageSource.getMessage(ex.getMessage(), null, LocaleContextHolder.getLocale());

    ValidationResponse response =
        new ValidationResponse(
            HttpStatus.BAD_REQUEST.value(),
            HttpStatus.BAD_REQUEST.getReasonPhrase(),
            LocalDateTime.now(),
            message,
            List.of(message));

    return response;
  }

  @ExceptionHandler(CardCreationApiException.class)
  public ResponseEntity<CardCreationApiErrorResponse> handleCardCreationApiException(CardCreationApiException ex, WebRequest request) {
    return ResponseEntity.status(ex.getStatus()).body(ex.getResponseBody());
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {
    final List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
    List<String> validationList = new ArrayList<>();
    for (FieldError fieldError : fieldErrors) {
      try {
        var beanClazz = ex.getTarget().getClass();
        var field = beanClazz.getDeclaredField(fieldError.getField());
        var jsonPropertyAnnotation = field.getAnnotation(JsonProperty.class);
        if (jsonPropertyAnnotation != null) {
          validationList.add(
              jsonPropertyAnnotation.value() + " -> " + fieldError.getDefaultMessage());
        } else {
          validationList.add(fieldError.getField() + " -> " + fieldError.getDefaultMessage());
        }
      } catch (NoSuchFieldException e) {
        validationList.add(fieldError.getField() + " -> " + fieldError.getDefaultMessage());
      }
    }

    String errorMessage = validationList.get(0);
    ValidationResponse message =
        new ValidationResponse(
            status.value(),
            ((HttpStatus) status).getReasonPhrase(),
            LocalDateTime.now(),
            errorMessage,
            validationList);
    return new ResponseEntity<>(message, status);
  }
}
