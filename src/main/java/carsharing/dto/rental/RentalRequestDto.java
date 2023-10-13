package carsharing.dto.rental;

import java.time.LocalDate;
import lombok.Data;

@Data
public class RentalRequestDto {
    private LocalDate rentalDate;
    private LocalDate returnDate;
    private LocalDate actualReturn;
    private Long userId;
    private Long carId;
}
