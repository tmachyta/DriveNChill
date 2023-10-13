package carsharing.mapper.rental;

import carsharing.config.MapperConfig;
import carsharing.dto.rental.RentalDto;
import carsharing.dto.rental.RentalRequestDto;
import carsharing.model.Rental;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface RentalMapper {

    RentalDto toDto(Rental rental);

    @Mapping(target = "id", ignore = true)
    Rental toModel(RentalRequestDto requestDto);
}
