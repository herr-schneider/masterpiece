package registry.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import registry.dtos.CreateAddresseeCommand;
import registry.services.AddresseService;
import registry.dtos.AddresseeDto;
import registry.validators.Violation;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Tag(name = "Addressee")
@RestController
@RequestMapping("/api/addressee")
public class AddresseeController {

    private AddresseService addresseService;

    public AddresseeController(AddresseService addresseService) {
        this.addresseService = addresseService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Listing all addresse", description = "Be carefull, all of them will be listed.")
    @ApiResponse(responseCode = "404", description = "There is not any of addressee!")
    public List<AddresseeDto> listAllAddressee() {
        return addresseService.listAll();
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Listing an addresse", description = "Addressee search by name")
    @ApiResponse(responseCode = "404", description = "There is not any of addressee!")
    public List<AddresseeDto> listAddresseeByName(@RequestParam Optional<String> name) {
        return addresseService.getAddresseeByName(name);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Listing an addresse", description = "Addressee search by id")
    @ApiResponse(responseCode = "404", description = "There is not any of addressee!")
    public AddresseeDto getAddressee(@PathVariable("id") long id) {
        return addresseService.getAddressee(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "An addressee can be created.",
            description = "An addressee without package.")
    public AddresseeDto createTrainingClass(@Valid @RequestBody CreateAddresseeCommand command) {
        return addresseService.createAddressee(command);
    }

    @PutMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update an Addressee", description = "Update a \"Addressee\" with Name")
    @ApiResponse(responseCode = "404", description = "Package not found")
    public ResponseEntity updateWithName(@RequestParam Optional<String> name,
                                         @Valid @RequestBody CreateAddresseeCommand command) {
        try {
            return ResponseEntity.ok(addresseService.updateWithName(name, command));
        } catch (IllegalArgumentException iae) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update an Addressee", description = "Update a \"Addressee\" with Name")
    @ApiResponse(responseCode = "404", description = "Package not found")
    public ResponseEntity updateWithId(@PathVariable("id") long id,
                                       @Valid @RequestBody CreateAddresseeCommand command) {
        try {
            return ResponseEntity.ok(addresseService.updateWithId(id, command));
        } catch (IllegalArgumentException iae) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete all addressee", description = "Never do this!")
    @ApiResponse(responseCode = "404", description = "Addressee not found")
    public void deleteAll() {
        addresseService.delAll();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete an addressee", description = "Del a addressee with ID!")
    @ApiResponse(responseCode = "404", description = "Addressee not found")
    public AddresseeDto deleteById(@PathVariable("id") long id) {
        return addresseService.delById(id);
    }

    @DeleteMapping("/delByName")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete an addressee", description = "Delete a addressee by addressee's name")
    @ApiResponse(responseCode = "404", description = "Addressee not found")
    public AddresseeDto updateWithDoku(@RequestParam Optional<String> name) {
        return addresseService.delByName(name);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Problem> handleValidException(MethodArgumentNotValidException mnve) {
        List<Violation> violations = mnve.getBindingResult().getFieldErrors().stream()
                .map(fe -> new Violation(fe.getField(), fe.getDefaultMessage()))
                .collect(Collectors.toList());

        Problem problem = Problem.builder()
                .withType(URI.create("/api/registry/not-valid"))
                .withTitle("Validation error")
                .withStatus(Status.BAD_REQUEST)
                .withDetail(mnve.getMessage())
                .with("violations", violations)
                .build();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(problem);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Problem> handleNotFound(IllegalArgumentException iae) {
        Problem problem = Problem.builder()
                .withType(URI.create("/api/emp/param"))
                .withTitle("Not found!")
                .withStatus(Status.NOT_FOUND)
                .withDetail(iae.getMessage())
                .build();
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(problem);
    }
}

