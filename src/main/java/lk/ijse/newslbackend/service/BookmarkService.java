package lk.ijse.newslbackend.service;

import lk.ijse.newslbackend.dto.ArticleRequestDTO;
import lk.ijse.newslbackend.dto.ArticleResponseDTO;
import lk.ijse.newslbackend.dto.BookmarkDTO;
import org.springframework.stereotype.Service;

import java.util.List;
@Service

public interface BookmarkService {
    void saveBookmark(String username, String articleId);
    void deleteBookmark(Long id);
    List<ArticleResponseDTO> getAllBookmarks(String username);
}
