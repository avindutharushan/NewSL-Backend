package lk.ijse.newslbackend.repository;

import lk.ijse.newslbackend.entity.IdCounter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
/**
 * This is the repository layer which is responsible for managing the id counter data in the database
 */
@Repository
public interface IdCounterRepository extends JpaRepository<IdCounter, Long> {
    Optional<IdCounter> findByPrefix(String prefix);
}