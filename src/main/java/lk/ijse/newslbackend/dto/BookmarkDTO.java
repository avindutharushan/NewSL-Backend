package lk.ijse.newslbackend.dto;

import lk.ijse.newslbackend.customObj.BookmarkResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BookmarkDTO implements BookmarkResponse {
    private ArticleResponseDTO article;
}
