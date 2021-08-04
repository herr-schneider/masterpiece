package registry.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import registry.entities.Addressee;
import registry.entities.Sender;
import registry.entities.StorageStatus;
import registry.validators.Doku;
import registry.validators.Name;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArrivalCommand {

    @Doku
    @Schema(description = "Doku number", example = "1234567890")
    private String doku;


    @Schema(description = "Last four digit of Customs Number", example = "0123", defaultValue = "1234")
    private String HU_num;

    @Schema(description = "Arrival date of package", example = "2021-08-03", defaultValue = "2021-08-03")
    private LocalDate arrival;

    @Name
    @Schema(description = "Name of sender")
    private Sender sender;

    @Name
    @Schema(description = "Name of addresse")
    private Addressee addressee;

    private StorageStatus storageStatus;

}
