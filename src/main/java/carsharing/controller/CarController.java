package carsharing.controller;

import carsharing.dto.car.CarDto;
import carsharing.dto.car.CreateCarRequestDto;
import carsharing.service.car.CarService;
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

@Tag(name = "Car management", description = "Endpoints for managing cars")
@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value = "/cars")
public class CarController {
    private final CarService carService;

    @PreAuthorize("hasAnyRole('CUSTOMER', 'MANAGER')")
    @GetMapping
    @Operation(summary = "Get all cars", description = "Get a list of all available cars")
    public List<CarDto> findAll(@ParameterObject Pageable pageable) {
        return carService.getAll(pageable);
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping
    @Operation(summary = "Save car to repository", description = "Save valid car to repository")
    public CarDto save(@RequestBody @Valid CreateCarRequestDto requestDto) {
        return carService.save(requestDto);
    }

    @PreAuthorize("hasAnyRole('CUSTOMER', 'MANAGER')")
    @GetMapping("/{id}")
    @Operation(summary = "Get car by id", description = "Get available car by id")
    public CarDto getCarById(@PathVariable Long id) {
        return carService.findById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete car by id", description = "Soft delete of available car by id")
    public void deleteById(@PathVariable Long id) {
        carService.deleteById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    @Operation(summary = "Update car by id", description = "Update available car by id")
    public CarDto update(@PathVariable Long id,
                          @RequestBody @Valid CreateCarRequestDto requestDto) {
        return carService.update(id, requestDto);
    }
}
