package lk.ijse.newslbackend.controller;

import lk.ijse.newslbackend.dto.ArticleResponseDTO;
import lk.ijse.newslbackend.dto.BookmarkDTO;
import lk.ijse.newslbackend.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bookmark")
@RequiredArgsConstructor
public class BookmarkController {
    private final BookmarkService bookmarkService;
    /**
     * {@code POST /} : Create a new Bookmark.
     * @param articleId the email of the user
     * @param username the username of the user
     * @return the {@link ResponseEntity} with status {@code 201 (Created)}.
     */
    @PreAuthorize("hasAnyRole('PREMIUM_USER', 'ADMIN')")
    @PostMapping(value = "/save")
    public ResponseEntity<Void> saveBookmark( @RequestParam("username") String username, @RequestParam("articleId") String articleId) {
        bookmarkService.saveBookmark(username, articleId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    /**
     * {@code DELETE /{id}} : Deletes an existing Bookmark.
     *
     * @param id the id of the Bookmark to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (No Content)}.
     */
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteBookmark(@PathVariable("id") Long id) {
        bookmarkService.deleteBookmark(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    /**
     * {@code GET /} : Get all Bookmarks for a user.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body all Bookmarks.
     */
    @PreAuthorize("hasAnyRole('PREMIUM_USER', 'ADMIN')")
    @GetMapping(value = "/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ArticleResponseDTO>> getAllBookmarks(@PathVariable("username") String username) {
        return ResponseEntity.status(HttpStatus.OK).body(bookmarkService.getAllBookmarks(username));
    }
}
