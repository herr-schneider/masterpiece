package registry.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DokuValidator implements ConstraintValidator<Doku, String> {

    @Override
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {
        if (name==null || name.isEmpty() || name.isBlank()){return false;}
        for (char c : name.toCharArray()){
           if (!Character.isDigit(c)) {
               return false;
           }
        }
        return name.trim().length()==10;
        }
}
