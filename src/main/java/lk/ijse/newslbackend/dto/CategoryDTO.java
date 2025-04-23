package lk.ijse.newslbackend.dto;

import lk.ijse.newslbackend.customObj.CategoryResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CategoryDTO implements CategoryResponse {
    private Long id;
    private String description;
    private String name;
}
