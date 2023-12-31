package carsharing.service.payment;

import carsharing.dto.payment.CreateRequestPaymentDto;
import carsharing.dto.payment.PaymentDto;
import carsharing.dto.payment.PaymentDtoResponse;
import carsharing.exception.CustomPaymentException;
import carsharing.exception.EntityNotFoundException;
import carsharing.mapper.payment.PaymentMapper;
import carsharing.model.Payment;
import carsharing.repository.payment.PaymentRepository;
import carsharing.service.telegram.TelegramNotificationService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final TelegramNotificationService telegramNotificationService;

    @Value("${stripe.secretKey}")
    private String stripeSecretKey;

    @Override
    public List<PaymentDto> getAll(Pageable pageable) {
        return paymentRepository.findAll(pageable)
                .stream()
                .map(paymentMapper::toDto)
                .toList();
    }

    @Override
    public PaymentDto save(CreateRequestPaymentDto requestDto) {
        Payment payment = paymentMapper.toModel(requestDto);
        Payment savedPayment = paymentRepository.save(payment);
        telegramNotificationService.sendNotification(
                "New payment saved " + savedPayment.getStatus());
        return paymentMapper.toDto(savedPayment);
    }

    @Override
    public PaymentDto findById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(()
                        -> new EntityNotFoundException("Can't find payment by id " + id));
        telegramNotificationService.sendNotification("Payment found: " + payment.getStatus());
        return paymentMapper.toDto(payment);
    }

    @Override
    public PaymentDto findPaymentByRentalId(Long rentalId) {
        Payment payment = paymentRepository.findByRentalId(rentalId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Can't find payment by rentalId " + rentalId));
        telegramNotificationService.sendNotification(
                "Payment found by rental ID: " + payment.getStatus());
        return paymentMapper.toDto(payment);
    }

    @Override
    public PaymentDto findPaymentByUserId(Long userId) {
        Payment payment = paymentRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Can;t find payment by userId" + userId));
        telegramNotificationService.sendNotification(
                "Payment found by user ID: " + payment.getStatus());
        return paymentMapper.toDto(payment);
    }

    @Override
    public PaymentDtoResponse createPaymentSession() throws CustomPaymentException {
        Stripe.apiKey = stripeSecretKey;

        try {
            PaymentIntentCreateParams params = new PaymentIntentCreateParams.Builder()
                    .setAmount(1000L)
                    .setCurrency("usd")
                    .setDescription("Car Rental Payment")
                    .setConfirmationMethod(PaymentIntentCreateParams.ConfirmationMethod.MANUAL)
                    .setConfirm(true)
                    .addPaymentMethodType("card")
                    .build();

            PaymentIntent paymentIntent = PaymentIntent.create(params);

            String paymentIntentId = paymentIntent.getId();

            PaymentDtoResponse paymentDto = new PaymentDtoResponse();
            paymentDto.setSessionUrl(new URL(paymentIntent.getClientSecret()));
            paymentDto.setCurrency("usd");
            paymentDto.setDescription("Car Rental Payment");
            paymentDto.setAmount(BigDecimal.valueOf(10.00));
            paymentDto.setPaymentIntentId(paymentIntentId);

            telegramNotificationService.sendNotification("Payment session created");
            return paymentDto;
        } catch (StripeException e) {
            throw new CustomPaymentException(
                    "Error creating payment session with Stripe: " + e.getMessage());
        } catch (MalformedURLException e) {
            throw new CustomPaymentException(
                    "Malformed URL when creating payment session: " + e.getMessage());
        }
    }

    @Override
    public PaymentDtoResponse checkSuccessfulPayment(String paymentIntentId)
            throws CustomPaymentException {
        Stripe.apiKey = stripeSecretKey;

        try {
            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);

            PaymentDtoResponse paymentDto = new PaymentDtoResponse();
            paymentDto.setStatus(Payment.Status.valueOf(paymentIntent.getStatus().toUpperCase()));

            paymentDto.setAmount(BigDecimal.valueOf(paymentIntent.getAmount() / 100.0));
            paymentDto.setCurrency(paymentIntent.getCurrency());
            paymentDto.setDescription(paymentIntent.getDescription());

            telegramNotificationService.sendNotification(
                    "Payment checked: " + paymentDto.getStatus());
            return paymentDto;
        } catch (StripeException e) {
            throw new CustomPaymentException(
                    "Error checking payment status with Stripe: " + e.getMessage());
        }
    }

    @Override
    public String handleCanceledPayment(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Can't find payment by id " + id));

        if (payment == null) {
            telegramNotificationService.sendNotification("Payment not found");
            return "Payment not found.";
        }
        if (payment.getStatus() == Payment.Status.PAID) {
            telegramNotificationService.sendNotification("Payment has already been processed");
            return "Your payment has already been processed and marked as PAID.";
        } else if (payment.getStatus() == Payment.Status.PENDING) {
            telegramNotificationService.sendNotification("Payment is pending");
            return "Your payment is currently pending. It will be processed soon.";
        } else {
            telegramNotificationService.sendNotification("Unknown payment status");
            return "Payment status unknown. Please contact our support team for assistance.";
        }
    }
}
