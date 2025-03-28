package lk.ijse.newslbackend.service;

import lk.ijse.newslbackend.dto.ArticleRequestDTO;
import lk.ijse.newslbackend.dto.ArticleResponseDTO;

import java.util.List;

public interface ArticleService {
    void saveArticle(ArticleRequestDTO articleRequestDTO);
    void updateArticle(String id, ArticleRequestDTO articleRequestDTO);
    void deleteArticle(String id);
    ArticleResponseDTO getSelectedArticle(String id);
    List<ArticleResponseDTO> getAllArticles();
}
