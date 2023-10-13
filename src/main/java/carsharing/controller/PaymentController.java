package carsharing.controller;

import carsharing.dto.payment.CreateRequestPaymentDto;
import carsharing.dto.payment.PaymentDto;
import carsharing.dto.payment.PaymentDtoResponse;
import carsharing.exception.CustomPaymentException;
import carsharing.service.payment.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Payment management", description = "Endpoints for managing payment")
@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value = "/payments")
public class PaymentController {
    private final PaymentService paymentService;

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping
    @Operation(summary = "Get all payments", description = "Get a list of all available pay,ents")
    public List<PaymentDto> findAll(@ParameterObject Pageable pageable) {
        return paymentService.getAll(pageable);
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping
    @Operation(summary = "Save payment to repository",
            description = "Save valid payment to repository")
    public PaymentDto save(@RequestBody @Valid CreateRequestPaymentDto requestDto) {
        return paymentService.save(requestDto);
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/{id}")
    @Operation(summary = "Get payment by id", description = "Get available payment by id")
    public PaymentDto getById(@PathVariable Long id) {
        return paymentService.findById(id);
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/rental/{id}")
    @Operation(summary = "Get payment by rentalId",
            description = "Get available payment by rentalId")
    public PaymentDto getPaymentByRentalId(@PathVariable Long rentalId) {
        return paymentService.findPaymentByRentalId(rentalId);
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/user/{id}")
    @Operation(summary = "Get payment by rentalId",
            description = "Get available payment by rentalId")
    public PaymentDto getPaymentByUserId(@PathVariable Long userId) {
        return paymentService.findPaymentByUserId(userId);
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("/create-session")
    @Operation(summary = "Create success payment sessions",
            description = "Create available payment session")
    public PaymentDtoResponse createPaymentSession() throws CustomPaymentException {
        return paymentService.createPaymentSession();
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/success")
    @Operation(summary = "Get success payment sessions",
            description = "Get available payment success")
    public PaymentDtoResponse checkSuccessfulPayment(
            @RequestParam("paymentIntentId") String paymentIntentId)
            throws CustomPaymentException {
        return paymentService.checkSuccessfulPayment(paymentIntentId);
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/cancel/{id}")
    @Operation(summary = "Get cancel payment", description = "Get available cancel payment")
    public String handleCanceledPayment(@PathVariable Long id) {
        return paymentService.handleCanceledPayment(id);
    }
}
