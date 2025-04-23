package lk.ijse.newslbackend.service;

import lk.ijse.newslbackend.dto.ArticleRequestDTO;
import lk.ijse.newslbackend.dto.ArticleResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;
@Service

public interface ArticleService {
    void saveArticle(ArticleRequestDTO articleRequestDTO);
    void updateArticle(String id, ArticleRequestDTO articleRequestDTO);
    void deleteArticle(String id);
    ArticleResponseDTO getSelectedArticle(String id);
    List<ArticleResponseDTO> getAllArticles();
}
