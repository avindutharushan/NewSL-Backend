package lk.ijse.newslbackend.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lk.ijse.newslbackend.dto.ArticleRequestDTO;
import lk.ijse.newslbackend.dto.ArticleResponseDTO;
import lk.ijse.newslbackend.entity.Article;
import lk.ijse.newslbackend.entity.enums.ArticleStatus;
import lk.ijse.newslbackend.entity.enums.IdPrefix;
import lk.ijse.newslbackend.exception.ArticleNotFoundException;
import lk.ijse.newslbackend.exception.DataPersistFailedException;
import lk.ijse.newslbackend.exception.FileConversionException;
import lk.ijse.newslbackend.repository.ArticleRepository;
import lk.ijse.newslbackend.repository.UserRepository;
import lk.ijse.newslbackend.service.ArticleService;
import lk.ijse.newslbackend.util.CustomIdGenerator;
import lk.ijse.newslbackend.util.Mapping;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class ArticleServiceImpl implements ArticleService {
    private final CustomIdGenerator customIdGenerator;
    private final Mapping mapping;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    @Autowired
    private Cloudinary cloudinary;
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final String[] ALLOWED_IMG_TYPES = {"image/jpeg", "image/png", "image/jpg", "image/gif"};


    @Override
    @Transactional
    public void saveArticle(ArticleRequestDTO articleSaveDTO) {
        try {
            articleSaveDTO.setId(customIdGenerator.generateId(IdPrefix.ARTICLE.getPrefix()));
            Article article = mapping.convertToEntity(articleSaveDTO, Article.class);
            userRepository.findByUsername(articleSaveDTO.getReporter()).ifPresent(
                    user -> article.setReporter(user)
            );
            article.setStatus(ArticleStatus.PENDING_REVIEW);
            article.setPublishedAt(LocalDateTime.now());

            saveImageInCloudinary(articleSaveDTO.getCoverImage(), article);

            articleRepository.save(article);
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataPersistFailedException("Cannot Save Article", 0, e);
        }
    }

    private void saveImageInCloudinary(MultipartFile image, Article article) {
        try {
            // Validate file size and type
            if (image.getSize() > MAX_FILE_SIZE) {
                throw new FileConversionException("File size must be less than 5MB");
            }
            if (!isValidImageType(image.getContentType())) {
                throw new FileConversionException("Only JPG, PNG, and GIF are supported");
            }

            // Upload to Cloudinary
            Map uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.asMap(
                    "public_id", "coverImage/" + article.getId() + "_" + System.currentTimeMillis(),
                    "overwrite", true,
                    "resource_type", "image"
            ));

            String imageUrl = (String) uploadResult.get("secure_url");

            // Delete old image from Cloudinary if it exists
            if (article.getCoverImage() != null) {
                String oldPublicId = extractPublicIdFromUrl(article.getCoverImage());
                cloudinary.uploader().destroy(oldPublicId, ObjectUtils.emptyMap());
            }

            // Update user with Cloudinary URL
            article.setCoverImage(imageUrl);

        } catch (IOException e) {
            throw new FileConversionException("Error uploading image: " ,e);
        }
    }
    private boolean isValidImageType(String contentType) {
        return contentType != null && Arrays.asList(ALLOWED_IMG_TYPES).contains(contentType.toLowerCase());
    }
    private String extractPublicIdFromUrl(String url) {
        try {
            String[] parts = url.split("/");
            String fileName = parts[parts.length - 1];

            int dotIndex = fileName.lastIndexOf(".");
            if (dotIndex == -1) {
                return "coverImage/" + fileName;
            } else {
                return "coverImage/" + fileName.substring(0, dotIndex);
            }
        } catch (Exception e) {
            // Handle unexpected URL formats gracefully
            throw new FileConversionException("Invalid Cloudinary URL format: " + url, e);
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
