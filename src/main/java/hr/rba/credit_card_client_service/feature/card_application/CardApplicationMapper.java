package hr.rba.credit_card_client_service.feature.card_application;

import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CardApplicationMapper {
  CardApplication toEntity(CardApplicationRequest cardApplicationRequest);

  CardApplicationResponse toResponse(CardApplication cardApplication);

  CardApplicationRequest toRequest(CardApplication cardApplication);
}
