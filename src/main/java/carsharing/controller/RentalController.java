package carsharing.controller;

import carsharing.dto.rental.RentalDto;
import carsharing.dto.rental.RentalRequestDto;
import carsharing.service.rental.RentalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Rental management", description = "Endpoints for managing rentals")
@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value = "/rentals")
public class RentalController {
    private final RentalService rentalService;

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping
    @Operation(summary = "Get all rentals", description = "Get a list of all available rentals")
    public List<RentalDto> findAll(@ParameterObject Pageable pageable) {
        return rentalService.getAll(pageable);
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping
    @Operation(summary = "Save rental to repository",
            description = "Save valid rental to repository")
    public RentalDto save(@RequestBody @Valid RentalRequestDto requestDto) {
        return rentalService.save(requestDto);
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/{id}")
    @Operation(summary = "Get rental by id", description = "Get available rental by id")
    public RentalDto getRentalById(@PathVariable Long id) {
        return rentalService.findById(id);
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/car/{id}")
    @Operation(summary = "Get rental by carId", description = "Get available rental by carId")
    public RentalDto getRentalByCarId(@PathVariable Long carId) {
        return rentalService.findByCarId(carId);
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/user/{id}")
    @Operation(summary = "Get rental by userId", description = "Get available rental by userId")
    public RentalDto getRentalByUserId(@PathVariable Long userId) {
        return rentalService.findByUserId(userId);
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PutMapping("/{id}")
    @Operation(summary = "Update rental by id", description = "Update available rental by id")
    public RentalDto update(@PathVariable Long id,
                         @RequestBody @Valid RentalRequestDto requestDto) {
        return rentalService.returnRental(id, requestDto);
    }

    @PreAuthorize("hasRole('MANAGER')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete rental by id",
            description = "Soft delete of available rental by id")
    public void deleteById(@PathVariable Long id) {
        rentalService.deleteById(id);
    }
}
