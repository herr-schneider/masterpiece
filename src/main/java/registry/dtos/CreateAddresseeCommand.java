package registry.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import registry.entities.Person;
import registry.entities.PostPackage;
import registry.validators.Name;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.CascadeType.REMOVE;

@Name
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAddresseeCommand extends Person{

    private String name;

    private String address;

    public CreateAddresseeCommand(String name) {
        this.name = name;
    }

}
