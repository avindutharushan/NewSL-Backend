package lk.ijse.newslbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "reporter_profiles")
public class ReporterProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(columnDefinition = "TEXT")
    private String bio;

    private String expertise;

    @Column(name = "social_media_links", columnDefinition = "TEXT")
    private String socialMediaLinks;

    @Column(name = "joined_date")
    private LocalDate joinedDate;

    @Column(name = "article_count")
    private int articleCount = 0;
}
