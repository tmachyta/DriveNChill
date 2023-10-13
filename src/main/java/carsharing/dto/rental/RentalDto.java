package carsharing.dto.rental;

import java.time.LocalDate;
import lombok.Data;

@Data
public class RentalDto {
    private Long id;
    private LocalDate rentalDate;
    private LocalDate returnDate;
    private LocalDate actualReturn;
    private Long userId;
    private Long carId;
}
