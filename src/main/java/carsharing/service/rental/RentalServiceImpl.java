package carsharing.service.rental;

import carsharing.dto.rental.RentalDto;
import carsharing.dto.rental.RentalRequestDto;
import carsharing.exception.CarNotAvailableException;
import carsharing.exception.EntityNotFoundException;
import carsharing.exception.IllegalArgumentException;
import carsharing.mapper.rental.RentalMapper;
import carsharing.model.Car;
import carsharing.model.Rental;
import carsharing.model.User;
import carsharing.repository.car.CarRepository;
import carsharing.repository.rental.RentalRepository;
import carsharing.repository.user.UserRepository;
import carsharing.service.telegram.TelegramNotificationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RentalServiceImpl implements RentalService {
    private final RentalRepository rentalRepository;
    private final RentalMapper rentalMapper;
    private final CarRepository carRepository;
    private final UserRepository userRepository;
    private final TelegramNotificationService telegramNotificationService;

    @Override
    public List<RentalDto> getAll(Pageable pageable) {
        return rentalRepository.findAll(pageable)
                .stream()
                .map(rentalMapper::toDto)
                .toList();
    }

    @Override
    public RentalDto save(RentalRequestDto requestDto) {
        Car car = carRepository.findById(requestDto.getCarId())
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Can't find car by id " + requestDto.getCarId()));
        if (car.getInventory() <= 0) {
            throw new CarNotAvailableException("Car is not available for rental");
        }

        car.setInventory(car.getInventory() - 1);
        carRepository.save(car);

        Rental rental = rentalMapper.toModel(requestDto);
        Rental savedRental = rentalRepository.save(rental);
        telegramNotificationService.sendNotification("New rental saved: " + savedRental.getId());
        return rentalMapper.toDto(savedRental);
    }

    @Override
    public RentalDto findById(Long id) {
        Rental rental = rentalRepository.findById(id)
                .orElseThrow(()
                        -> new EntityNotFoundException("Can't find rental by id " + id));
        telegramNotificationService.sendNotification("Rental found: " + rental.getId());
        return rentalMapper.toDto(rental);
    }

    @Override
    public RentalDto findByCarId(Long carId) {
        Rental rental = rentalRepository.findByCarId(carId);
        if (rental == null) {
            throw new EntityNotFoundException("Can't find rental by carId " + carId);
        }
        telegramNotificationService.sendNotification("Rental found by carId: " + rental.getId());
        return rentalMapper.toDto(rental);
    }

    @Override
    public RentalDto findByUserId(Long userId) {
        Rental rental = rentalRepository.findByUserId(userId);
        if (rental == null) {
            throw new EntityNotFoundException("Can't find rental by userId " + userId);
        }
        telegramNotificationService.sendNotification("Rental found by userId: " + rental.getId());
        return rentalMapper.toDto(rental);
    }

    @Override
    public RentalDto returnRental(Long id, RentalRequestDto requestDto) {
        Long carId = requestDto.getCarId();
        Long userId = requestDto.getUserId();

        if (carId == null || userId == null) {
            throw new IllegalArgumentException("CarId and UserId must be provided");
        }

        Car car = carRepository.findById(requestDto.getCarId())
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Can't find car by id " + requestDto.getCarId()));
        if (car.getInventory() <= 0) {
            throw new CarNotAvailableException("Car is not available for rental");
        }
        car.setInventory(car.getInventory() + 1);
        carRepository.save(car);

        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Can't find user by id " + requestDto.getUserId()));

        Rental existedRental = rentalRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Can't find rental by id " + id));
        existedRental.setRentalDate(requestDto.getRentalDate());
        existedRental.setReturnDate(requestDto.getReturnDate());
        existedRental.setActualReturn(requestDto.getActualReturn());
        existedRental.setUser(user);
        existedRental.setCar(car);
        telegramNotificationService.sendNotification("Rental returned: " + id);
        return rentalMapper.toDto(rentalRepository.save(existedRental));
    }

    @Override
    public void deleteById(Long id) {
        telegramNotificationService.sendNotification("Rental deleted: " + id);
        rentalRepository.deleteById(id);
    }
}
