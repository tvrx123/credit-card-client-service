package hr.rba.credit_card_client_service.configuration.validation;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ValidationResponse {
  @Schema(example = "400")
  private int statusCode;
  @Schema(example = "Bad Request")
  private String statusType;
  @Schema(example = "2024-10-21T13:13:36.461036")
  private LocalDateTime timestamp;
  @Schema(example = "Zahtjev za izdavanjem kartice sa zadanim OIB-om nije pronađen!")
  private String message;
  @ArraySchema(schema = @Schema(example = "Zahtjev za izdavanjem kartice sa zadanim OIB-om nije pronađen!"))
  private List<String> validationList;
}
