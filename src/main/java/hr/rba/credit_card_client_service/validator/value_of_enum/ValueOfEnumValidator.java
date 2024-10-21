package hr.rba.credit_card_client_service.validator.value_of_enum;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ValueOfEnumValidator implements ConstraintValidator<ValueOfEnum, CharSequence> {
  private final MessageSource messageSource;
  private List<String> acceptedValues;

  @Override
  public void initialize(ValueOfEnum annotation) {
    acceptedValues =
        Stream.of(annotation.enumClass().getEnumConstants())
            .map(Enum::name)
            .collect(Collectors.toList());
  }

  @Override
  public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
    if (value == null) {
      return true;
    }

    if (!acceptedValues.contains(value.toString())) {
      context.disableDefaultConstraintViolation();
      context
          .buildConstraintViolationWithTemplate(
              messageSource.getMessage("valueOfEnum.message", null, LocaleContextHolder.getLocale())
                  + acceptedValues)
          .addConstraintViolation();
      return false;
    }
    return true;
  }
}
