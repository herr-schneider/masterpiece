package registry.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = DokuValidator.class)
public @interface Doku {

    String message() default "Invalid Doku number!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}