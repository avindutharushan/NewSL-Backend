package lk.ijse.newslbackend.dto;

import jakarta.validation.constraints.NotBlank;
import lk.ijse.newslbackend.customObj.ArticleResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ArticleRequestDTO implements ArticleResponse {
    private String id;
    @NotBlank
    private String reporter;
    @NotBlank
    private String title;
    @NotBlank
    private String content;
    private MultipartFile coverImage;
}
