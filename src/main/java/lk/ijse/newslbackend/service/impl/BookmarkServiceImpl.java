package lk.ijse.newslbackend.service.impl;

import lk.ijse.newslbackend.dto.ArticleResponseDTO;
import lk.ijse.newslbackend.dto.BookmarkDTO;
import lk.ijse.newslbackend.entity.Article;
import lk.ijse.newslbackend.entity.Bookmark;
import lk.ijse.newslbackend.entity.User;
import lk.ijse.newslbackend.exception.ArticleNotFoundException;
import lk.ijse.newslbackend.exception.BookmarkNotFoundException;
import lk.ijse.newslbackend.exception.DataPersistFailedException;
import lk.ijse.newslbackend.repository.ArticleRepository;
import lk.ijse.newslbackend.repository.BookmarkRepository;
import lk.ijse.newslbackend.repository.UserRepository;
import lk.ijse.newslbackend.service.BookmarkService;
import lk.ijse.newslbackend.util.Mapping;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookmarkServiceImpl implements BookmarkService {
    private final Mapping mapping;
    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;

    @Override
    public void saveBookmark(String username, String articleId) {
        try {
            User byUsername = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            Article article = articleRepository.findById(articleId)
                    .orElseThrow(() -> new ArticleNotFoundException("Article not found"));

            Bookmark bookmark = Bookmark.builder()
                            .user(byUsername)
                                    .article(article)
                                            .build();
            bookmarkRepository.save(bookmark);
        } catch (Exception e) {
            throw new DataPersistFailedException("Cannot Save Article", 0, e);
        }
    }

    @Override
    public void deleteBookmark(Long id) {
        Bookmark bookmark = bookmarkRepository.findById(id)
                .orElseThrow(() -> new BookmarkNotFoundException("Bookmark not found"));
        bookmarkRepository.delete(bookmark);
    }

    @Override
    public List<ArticleResponseDTO> getAllBookmarks(String username) {
        List<Bookmark> bookmarks = bookmarkRepository.findAllByUsername(username);
        if (bookmarks.isEmpty()) throw new BookmarkNotFoundException("No Bookmark found");
        List<ArticleResponseDTO> articles = new ArrayList<>();
        for (Bookmark bookmark : bookmarks) {
            Article article = articleRepository.findById(bookmark.getArticle().getId())
                    .orElseThrow(() -> new ArticleNotFoundException("Article not found"));
            ArticleResponseDTO articleResponseDTO = mapping.convertToDTO(article, ArticleResponseDTO.class);
            articles.add(articleResponseDTO);
        }
        return articles;
    }
}
