package lk.ijse.newslbackend.entity;

import jakarta.persistence.*;
import lk.ijse.newslbackend.entity.enums.ArticleStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "articles")
public class Article {
    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "reporter_id", nullable = false)
    private User reporter;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "cover_image")
    private String coverImage;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    @Column(name = "is_published")
    private boolean isPublished = false;

    @Column(name = "is_featured")
    private boolean isFeatured = false;

    @Column(name = "view_count")
    private int viewCount = 0;

    @Enumerated(EnumType.STRING)
    private ArticleStatus status = ArticleStatus.DRAFT;

    // Relationships
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @OneToMany(mappedBy = "article")
    private List<Bookmark> bookmarks;

    @OneToMany(mappedBy = "article")
    private List<Like> likes;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private List<MediaAttachment> mediaAttachments;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private List<ArticleCategory> articleCategories;

}