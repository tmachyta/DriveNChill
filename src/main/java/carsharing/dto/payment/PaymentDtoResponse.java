package carsharing.dto.payment;

import carsharing.model.Payment;
import java.math.BigDecimal;
import java.net.URL;
import lombok.Data;

@Data
public class PaymentDtoResponse {
    private Long id;
    private Long rentalId;
    private Payment.Status status;
    private Payment.Type type;
    private BigDecimal amount;
    private URL sessionUrl;
    private Long userId;
    private String currency;
    private String description;
    private String paymentIntentId;
}
