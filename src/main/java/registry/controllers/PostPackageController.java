package registry.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import registry.services.PostPackageService;
import registry.dtos.*;
import registry.validators.Violation;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Tag(name = "Registry")
@RestController
@RequestMapping("/api/registry")
public class PostPackageController {

    private PostPackageService postPackageService;

    public PostPackageController(PostPackageService postPackageService) {
        this.postPackageService = postPackageService;
    }

    @GetMapping("/packages")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Listing all packages", description = "Listing all packages without exhibits")
    @ApiResponse(responseCode = "404", description = "There is no packages!")
    public List<PostPackageDto> listAllPackage() {
        return postPackageService.listAll();
    }

    @PostMapping("/packages")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "A package can be created.",
            description = "A package and a case can be created without exhibits.")
    public PostPackageDto createTrainingClass(@Valid @RequestBody ArrivalCommand command) {
        return postPackageService.createPackage(command);
    }

    @PutMapping("/packages")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update a package", description = "Update a \"package\" with Doku number")
    @ApiResponse(responseCode = "404", description = "Package not found")
    public ResponseEntity updateWithDoku(@RequestParam Optional<String> dokuNumber,
                                         @Valid @RequestBody ArrivalCommand command) {
        try {
            return ResponseEntity.ok(postPackageService.updateWithDoku(dokuNumber, command));
        } catch (IllegalArgumentException iae) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete all packages", description = "Never do this!")
    @ApiResponse(responseCode = "404", description = "Package not found")
    public void deleteAll() {
        postPackageService.delAll();
    }

    @GetMapping("/packages/search")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Listing a package", description = "Listing a package with doku number")
    @ApiResponse(responseCode = "404", description = "There is no packages!")
    public ResponseEntity findByDoku(@RequestParam Optional<String> dokuNumber) {
        try {
            return ResponseEntity.ok(postPackageService.findByDoku(dokuNumber));
        } catch (IllegalArgumentException iae) {
            return ResponseEntity.notFound().build();
        }
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
