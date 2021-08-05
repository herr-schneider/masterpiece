package registry.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
@Constraint(validatedBy = NameValidator.class)
public @interface Name {

    String message() default "Invalid input text!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}