package lk.ijse.newslbackend.dto;

import lk.ijse.newslbackend.customObj.ArticleResponse;
import lk.ijse.newslbackend.entity.User;
import lk.ijse.newslbackend.entity.enums.ArticleStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ArticleResponseDTO implements ArticleResponse {
    private String id;
    private String reporter;
    private String title;
    private String content;
    private String coverImage;
    private LocalDateTime publishedAt;
    private boolean isPublished;
    private boolean isFeatured;
    private int viewCount;
    private ArticleStatus status;

    public void setReporter(User reporter) {
        this.reporter = reporter.getNewSLUsername();
    }
}
