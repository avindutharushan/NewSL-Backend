package lk.ijse.newslbackend.controller;

import lk.ijse.newslbackend.dto.ArticleResponseDTO;
import lk.ijse.newslbackend.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/home")
@RequiredArgsConstructor
public class DashbordController {
    private final ArticleService articleService;
    /**
     * {@code GET /} : Get all articles.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body all articles.
     */
    @PreAuthorize("hasAnyRole('REPORTER', 'ADMIN')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ArticleResponseDTO>> getAllArticles() {
        return ResponseEntity.status(HttpStatus.OK).body(articleService.getAllArticles());
    }

}
