package registry.dtos;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import registry.entities.Sender;
import registry.entities.StorageStatus;
import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostPackageDto {

    private Long id;

    private String doku;

    private String HU_num;

    private LocalDate arrival;

    private Sender sender;

    @JsonBackReference
    private AddresseeDto addressee;

    private StorageStatus storageStatus;
}
