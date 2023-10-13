package carsharing.service.rental;

import carsharing.dto.rental.RentalDto;
import carsharing.dto.rental.RentalRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface RentalService {

    List<RentalDto> getAll(Pageable pageable);

    RentalDto save(RentalRequestDto requestDto);

    RentalDto findById(Long id);

    RentalDto findByCarId(Long carId);

    RentalDto findByUserId(Long userId);

    RentalDto returnRental(Long id, RentalRequestDto requestDto);

    void deleteById(Long id);
}
