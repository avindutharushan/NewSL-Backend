package lk.ijse.newslbackend.customObj.impl;

import lk.ijse.newslbackend.customObj.BookmarkResponse;
import lk.ijse.newslbackend.customObj.CategoryResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BookmarkErrorResponse implements BookmarkResponse {
    private int errorCode;
    private String errorMessage;
}