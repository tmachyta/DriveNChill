package carsharing.mapper.payment;

import carsharing.config.MapperConfig;
import carsharing.dto.payment.CreateRequestPaymentDto;
import carsharing.dto.payment.PaymentDto;
import carsharing.model.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface PaymentMapper {
    PaymentDto toDto(Payment payment);

    @Mapping(target = "id", ignore = true)
    Payment toModel(CreateRequestPaymentDto requestDto);
}
