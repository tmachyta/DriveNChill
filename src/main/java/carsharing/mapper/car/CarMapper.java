package carsharing.mapper.car;

import carsharing.config.MapperConfig;
import carsharing.dto.car.CarDto;
import carsharing.dto.car.CreateCarRequestDto;
import carsharing.model.Car;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface CarMapper {

    CarDto toDto(Car car);

    @Mapping(target = "id", ignore = true)
    Car toModel(CreateCarRequestDto requestDto);
}
