package carsharing.service.car;

import carsharing.dto.car.CarDto;
import carsharing.dto.car.CreateCarRequestDto;
import carsharing.exception.EntityNotFoundException;
import carsharing.mapper.car.CarMapper;
import carsharing.model.Car;
import carsharing.repository.car.CarRepository;
import carsharing.service.telegram.TelegramNotificationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CarServiceImpl implements CarService {
    private final CarRepository carRepository;
    private final CarMapper carMapper;
    private final TelegramNotificationService telegramNotificationService;

    @Override
    public CarDto save(CreateCarRequestDto requestDto) {
        Car car = carMapper.toModel(requestDto);
        Car savedCar = carRepository.save(car);
        telegramNotificationService.sendNotification("New car created: " + savedCar.getModel());
        return carMapper.toDto(savedCar);
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
        telegramNotificationService.sendNotification("Car found: " + car.getModel());
        return carMapper.toDto(car);
    }

    @Override
    public void deleteById(Long id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find car by id " + id));
        carRepository.deleteById(id);
        telegramNotificationService.sendNotification("Car deleted: " + car.getModel());
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

        Car savedCar = carRepository.save(existedCar);

        telegramNotificationService.sendNotification("Car updated: " + savedCar.getModel());
        return carMapper.toDto(savedCar);
    }
}
