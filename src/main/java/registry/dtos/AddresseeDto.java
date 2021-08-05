package registry.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import registry.entities.PostPackage;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AddresseeDto {

    private long id;

    private String name;

    private String address;
}
