package hr.rba.credit_card_client_service.validator.value_of_enum;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({FIELD})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = ValueOfEnumValidator.class)
public @interface ValueOfEnum {
  Class<? extends Enum<?>> enumClass();

  String message() default "Value not allowed.";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
