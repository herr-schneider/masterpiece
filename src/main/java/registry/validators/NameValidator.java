package registry.validators;

import registry.entities.Person;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NameValidator implements ConstraintValidator<Name, Person> {

    @Override
    public boolean isValid(Person person, ConstraintValidatorContext constraintValidatorContext) {
        if (person.getName()==null || person.getName().isEmpty() || person.getName().isBlank()){
            System.out.println("Empty input");
            return false;}
        return person.getName().trim().length()>2 && person.getName().length()<50 &&
                Character.isUpperCase(person.getName().trim().charAt(0));
        }
}
