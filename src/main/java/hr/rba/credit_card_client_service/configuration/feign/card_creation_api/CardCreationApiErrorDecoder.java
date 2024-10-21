package hr.rba.credit_card_client_service.configuration.feign.card_creation_api;

import com.google.gson.Gson;
import feign.Response;
import feign.codec.ErrorDecoder;
import java.io.IOException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CardCreationApiErrorDecoder implements ErrorDecoder {

  private final Gson gson;
  @Override
  public Exception decode(String methodKey, Response response) {

    try {
      CardCreationApiErrorResponse apiErrorResponse =
          gson.fromJson(response.body().asReader(), CardCreationApiErrorResponse.class);
      return new CardCreationApiException(response.status(), apiErrorResponse);
    } catch (IOException e) {
      return new ErrorDecoder.Default().decode(methodKey, response);
    }
  }
}