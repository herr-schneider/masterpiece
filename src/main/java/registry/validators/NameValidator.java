package registry.validators;

import registry.entities.Person;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NameValidator implements ConstraintValidator<Name, Person> {

    int minLength;
    int maxLength;
    @Override
    public boolean isValid(Person person, ConstraintValidatorContext constraintValidatorContext) {
        if (person.getName()==null || person.getName().isEmpty() || person.getName().isBlank()){
            System.out.println("Empty input");
            return false;}
        return person.getName().trim().length()>minLength && person.getName().length()<maxLength &&
                Character.isUpperCase(person.getName().trim().charAt(0)); // && person.getName().trim().contains(" ");
        }

    @Override
    public void initialize(Name constraintAnnotation) {
        minLength = constraintAnnotation.minLength();
        maxLength = constraintAnnotation.maxLength();
    }
}
