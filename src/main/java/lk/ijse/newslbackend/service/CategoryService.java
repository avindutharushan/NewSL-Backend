package lk.ijse.newslbackend.service;

import lk.ijse.newslbackend.dto.CategoryDTO;
import org.springframework.stereotype.Service;

import java.util.List;
@Service

public interface CategoryService {
    void saveCategory(CategoryDTO categoryDTO);
    void updateCategory(Long id, CategoryDTO categoryDTO);

    void deleteCategory(Long id);

    CategoryDTO getSelectedCategory(Long id);
    List<CategoryDTO> getAllCategorys();
}
