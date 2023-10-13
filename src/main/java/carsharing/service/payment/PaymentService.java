package carsharing.service.payment;

import carsharing.dto.payment.CreateRequestPaymentDto;
import carsharing.dto.payment.PaymentDto;
import carsharing.dto.payment.PaymentDtoResponse;
import carsharing.exception.CustomPaymentException;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface PaymentService {

    List<PaymentDto> getAll(Pageable pageable);

    PaymentDto save(CreateRequestPaymentDto requestDto);

    PaymentDto findById(Long id);

    PaymentDto findPaymentByRentalId(Long rentalId);

    PaymentDto findPaymentByUserId(Long userId);

    PaymentDtoResponse createPaymentSession() throws CustomPaymentException;

    PaymentDtoResponse checkSuccessfulPayment(String paymentIntentId) throws CustomPaymentException;

    String handleCanceledPayment(Long id);
}
