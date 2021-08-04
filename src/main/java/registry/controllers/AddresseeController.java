package registry.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import registry.dtos.ArrivalCommand;
import registry.services.AddresseService;
import registry.dtos.AddresseeDto;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Tag(name = "Addressee")
@RestController
@RequestMapping("/api/adressee")
public class AddresseeController {

    private AddresseService addresseService;

    public AddresseeController(AddresseService addresseService) {
        this.addresseService = addresseService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Listing all addresse", description = "Be carefull, all of them will be listed.")
    @ApiResponse(responseCode = "404", description = "There is not any of addressee!")
    public List<AddresseeDto> listAllOpinion() {
        return addresseService.listAll();
    }

    @DeleteMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete all addressee", description = "Never do this!")
    @ApiResponse(responseCode = "404", description = "Addressee not found")
    public void deleteAll() {
        addresseService.delAll();
    }

    @DeleteMapping("/delByName")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete an addressee", description = "Delete a addressee by addressee's name")
    @ApiResponse(responseCode = "404", description = "Addressee not found")
    public AddresseeDto updateWithDoku(@RequestParam Optional<String> name) {
        return addresseService.delByName(name);
    }
}

