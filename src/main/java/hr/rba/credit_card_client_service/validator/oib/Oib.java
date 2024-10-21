package hr.rba.credit_card_client_service.validator.oib;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = OibValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Oib {

  String message() default "Field must be valid oib!";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
