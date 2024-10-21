package hr.rba.credit_card_client_service.validator.oib;


import static hr.rba.credit_card_client_service.util.checksum.Mod11_10.calculateChecksum;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OibValidator implements ConstraintValidator<Oib, String> {
  private final MessageSource messageSource;
  @Override
  public boolean isValid(String input, ConstraintValidatorContext context) {
    if (input == null) return true;
    if (input.length() != 11) return false;
    String checkDigit = input.substring(input.length() - 1);
    String data = input.substring(0, input.length() - 1);
    context.disableDefaultConstraintViolation();
    if(checkDigit.equals(calculateChecksum(data))){
      return true;
    }
    else{
      context
              .buildConstraintViolationWithTemplate(
                      messageSource.getMessage("oib.message", null, LocaleContextHolder.getLocale()))
              .addConstraintViolation();
      return false;
    }
  }
}
