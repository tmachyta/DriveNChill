package carsharing.service.car;

import carsharing.dto.car.CarDto;
import carsharing.dto.car.CreateCarRequestDto;
import carsharing.exception.EntityNotFoundException;
import carsharing.mapper.car.CarMapper;
import carsharing.model.Car;
import carsharing.repository.car.CarRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CarServiceImpl implements CarService {
    private final CarRepository carRepository;
    private final CarMapper carMapper;

    @Override
    public CarDto save(CreateCarRequestDto requestDto) {
        Car car = carMapper.toModel(requestDto);
        return carMapper.toDto(carRepository.save(car));
    }

    @Override
    public List<CarDto> getAll(Pageable pageable) {
        return carRepository.findAll(pageable)
                .stream()
                .map(carMapper::toDto)
                .toList();
    }

    @Override
    public CarDto findById(Long id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find car by id " + id));
        return carMapper.toDto(car);
    }

    @Override
    public void deleteById(Long id) {
        carRepository.deleteById(id);
    }

    @Override
    public CarDto update(Long id, CreateCarRequestDto requestDto) {
        Car existedCar = carRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find car by id " + id));
        existedCar.setModel(requestDto.getModel());
        existedCar.setBrand(requestDto.getBrand());
        existedCar.setType(requestDto.getType());
        existedCar.setInventory(requestDto.getInventory());
        existedCar.setDailyFee(requestDto.getDailyFee());
        return carMapper.toDto(carRepository.save(existedCar));
    }
}
