package uz.pdp.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.demo.entity.SalaryHistory;

import java.util.Optional;
import java.util.UUID;

public interface SalaryHistoryRepository extends JpaRepository<SalaryHistory, Integer> {
    Optional<SalaryHistory> findByUserId(UUID user_id);
}
