package lk.ijse.newslbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "user_preferences")
public class UserPreference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "email_notifications")
    private boolean emailNotifications = true;

    @Column(name = "push_notifications")
    private boolean pushNotifications = true;

    @Column(name = "preferred_categories")
    private String preferredCategories;
}