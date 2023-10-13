package carsharing.service.car;

import carsharing.dto.car.CarDto;
import carsharing.dto.car.CreateCarRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface CarService {

    CarDto save(CreateCarRequestDto requestDto);

    List<CarDto> getAll(Pageable pageable);

    CarDto findById(Long id);

    void deleteById(Long id);

    CarDto update(Long id, CreateCarRequestDto requestDto);
}
