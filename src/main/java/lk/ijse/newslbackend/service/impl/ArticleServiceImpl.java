package lk.ijse.newslbackend.service.impl;

import lk.ijse.newslbackend.dto.ArticleRequestDTO;
import lk.ijse.newslbackend.dto.ArticleResponseDTO;
import lk.ijse.newslbackend.entity.Article;
import lk.ijse.newslbackend.entity.enums.IdPrefix;
import lk.ijse.newslbackend.exception.ArticleNotFoundException;
import lk.ijse.newslbackend.exception.DataPersistFailedException;
import lk.ijse.newslbackend.repository.ArticleRepository;
import lk.ijse.newslbackend.service.ArticleService;
import lk.ijse.newslbackend.util.CustomIdGenerator;
import lk.ijse.newslbackend.util.Mapping;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {
    private final CustomIdGenerator customIdGenerator;
    private final Mapping mapping;
    private final ArticleRepository articleRepository;

    @Override
    public void saveArticle(ArticleRequestDTO articleSaveDTO) {
        try {
            articleSaveDTO.setCode(customIdGenerator.generateId(IdPrefix.ARTICLE.getPrefix()));
            Article article = mapping.convertToEntity(articleSaveDTO, Article.class);
            articleRepository.save(article);
        } catch (Exception e) {
            throw new DataPersistFailedException("Cannot Save Article", 0, e);
        }
    }

    @Override
    public void updateArticle(String id, ArticleRequestDTO articleSaveDTO) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ArticleNotFoundException("Article not found"));
        //update


    }

    @Override
    public void deleteArticle(String id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ArticleNotFoundException("Article not found"));
        articleRepository.delete(article);
    }

    @Override
    public ArticleResponseDTO getSelectedArticle(String id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ArticleNotFoundException("Article not found"));
        return mapping.convertToDTO(article, ArticleResponseDTO.class);
    }

    @Override
    public List<ArticleResponseDTO> getAllArticles() {
        List<Article> article = articleRepository.findAll();
        if (article.isEmpty()) throw new ArticleNotFoundException("No Articles found");
        return mapping.convertToDTOList(article, ArticleResponseDTO.class);
    }
}
