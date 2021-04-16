package uz.pdp.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.demo.dto.EmployeeInfoDto;
import uz.pdp.demo.entity.Turniket;

import java.util.Optional;
import java.util.UUID;

public interface TurniketRepository extends JpaRepository<Turniket, Integer> {

    Optional<Turniket> findByCreatedByAndStatus(UUID createdBy, boolean status);

    Optional<Turniket> findByCreatedBy(UUID createdBy);
    //
//    @Query(value = "select new uz.pdp.demo.dto.Attendance(tr.enterWork, tr.exitWork) from Turniket tr")
//    Optional<EmployeeInfoDto> getAttendanceByCreatedBy(UUID createdBy);
}
