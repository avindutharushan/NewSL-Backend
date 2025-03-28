package lk.ijse.newslbackend.customObj.impl;

import lk.ijse.newslbackend.customObj.ArticleResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ArticleErrorResponse implements ArticleResponse {
    private int errorCode;
    private String errorMessage;
}