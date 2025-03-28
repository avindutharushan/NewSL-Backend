package lk.ijse.newslbackend.repository;

import lk.ijse.newslbackend.entity.ReporterProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReporterProfileRepository extends JpaRepository<ReporterProfile,Long> {
}
