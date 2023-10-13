package carsharing.repository.rental;

import carsharing.model.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {
    Rental findByCarId(Long carId);

    Rental findByUserId(Long userId);
}
