package hr.rba.credit_card_client_service.feature.card_application;

import hr.rba.credit_card_client_service.validator.oib.Oib;
import hr.rba.credit_card_client_service.validator.value_of_enum.ValueOfEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CardApplicationRequest {
  @NotBlank
  @Schema(example = "Pero")
  private String firstName;
  @NotBlank
  @Schema(example = "Perić")
  private String lastName;
  @NotBlank
  @Oib
  @Schema(example = "21793867820")
  private String oib;
  @NotBlank
  @ValueOfEnum(enumClass = CardApplication.Status.class)
  @Schema(example = "Pending")
  private String status;
}
