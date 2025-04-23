package lk.ijse.newslbackend.customObj.impl;

import lk.ijse.newslbackend.customObj.ArticleResponse;
import lk.ijse.newslbackend.customObj.CategoryResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CategoryErrorResponse implements CategoryResponse {
    private int errorCode;
    private String errorMessage;
}