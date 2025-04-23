package lk.ijse.newslbackend.controller;

import jakarta.validation.Valid;
import lk.ijse.newslbackend.dto.CategoryDTO;
import lk.ijse.newslbackend.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    /**
     * {@code POST /} : Create a new Category.
     *
     * @param categoryDTO the Category to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)}.
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> saveCategory(@Valid @ModelAttribute CategoryDTO categoryDTO) {
        categoryService.saveCategory(categoryDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    /**
     * {@code PATCH /{id}} : Updates an existing Category.
     *
     * @param id the id of the Category to update.
     * @param categorySaveDTO the Category to update.
     * @return the {@link ResponseEntity} with status {@code 204 (No Content)}.
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateCategory(@PathVariable("id") Long id, @Valid @ModelAttribute CategoryDTO categorySaveDTO) {
        categoryService.updateCategory(id, categorySaveDTO);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    /**
     * {@code DELETE /{id}} : Deletes an existing Category.
     *
     * @param id the id of the Category to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (No Content)}.
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable("id") Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    /**
     * {@code GET /{id}} : Get the Category by id.
     *
     * @param id the id of the Category to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the Category.
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryDTO> getCategory(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.getSelectedCategory(id));
    }
    /**
     * {@code GET /} : Get all Category's.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body all Category's.
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CategoryDTO>> getAllCategorys() {
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.getAllCategorys());
    }
}
