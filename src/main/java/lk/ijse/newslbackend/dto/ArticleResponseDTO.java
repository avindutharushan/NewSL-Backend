package lk.ijse.newslbackend.dto;

import lk.ijse.newslbackend.customObj.ArticleResponse;
import lk.ijse.newslbackend.entity.enums.ArticleStatus;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

public class ArticleResponseDTO implements ArticleResponse {
    private String reporter;
    private String title;
    private String content;
    private MultipartFile coverImage;
    private LocalDateTime publishedAt;
    private boolean isPublished;
    private boolean isFeatured;
    private int viewCount;
    private ArticleStatus status;
}
