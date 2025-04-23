package lk.ijse.newslbackend.repository;

import lk.ijse.newslbackend.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
