package hr.rba.credit_card_client_service.feature.card_application;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CardApplicationResponse {
  @Schema(example = "Pero")
  private String firstName;

  @Schema(example = "Perić")
  private String lastName;

  @Schema(example = "21793867820")
  private String oib;

  @Schema(example = "Pending")
  private CardApplication.Status status;
}
