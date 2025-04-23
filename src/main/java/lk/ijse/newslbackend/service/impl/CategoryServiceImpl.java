package lk.ijse.newslbackend.service.impl;

import lk.ijse.newslbackend.dto.CategoryDTO;
import lk.ijse.newslbackend.entity.Category;
import lk.ijse.newslbackend.exception.CategoryNotFoundException;
import lk.ijse.newslbackend.exception.DataPersistFailedException;
import lk.ijse.newslbackend.repository.CategoryRepository;
import lk.ijse.newslbackend.service.CategoryService;
import lk.ijse.newslbackend.util.CustomIdGenerator;
import lk.ijse.newslbackend.util.Mapping;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final Mapping mapping;
    private final CustomIdGenerator customIdGenerator;

    @Override
    public void saveCategory(CategoryDTO categoryDTO) {
        try {
            Category category = mapping.convertToEntity(categoryDTO, Category.class);
            categoryRepository.save(category);
        } catch (Exception e) {
            throw new DataPersistFailedException("Cannot Save Category", 0, e);
        }
    }

    @Override
    public void updateCategory(Long id, CategoryDTO categoryDTO) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));
        //update


    }

    @Override
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));
        categoryRepository.delete(category);
    }

    @Override
    public CategoryDTO getSelectedCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));
        return mapping.convertToDTO(category, CategoryDTO.class);

    }

    @Override
    public List<CategoryDTO> getAllCategorys() {
        List<Category> category = categoryRepository.findAll();
        if (category.isEmpty()) throw new CategoryNotFoundException("No Categorys found");
        return mapping.convertToDTOList(category, CategoryDTO.class);

    }
}
