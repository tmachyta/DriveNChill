package carsharing.dto.car;

import carsharing.model.Car;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class CreateCarRequestDto {
    private String model;
    private String brand;
    private Car.Type type;
    private int inventory;
    private BigDecimal dailyFee;
}
