package lk.ijse.newslbackend.entity;

import jakarta.persistence.*;
import lk.ijse.newslbackend.entity.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "user")
public class User implements  UserDetails {
    @Id
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(name = "is_active")
    private boolean isActive = true;

    @Column(name = "profile_picture")
    private String profilePicture;

    // Relationships
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserPreference userPreference;

    @OneToMany(mappedBy = "user")
    private List<Comment> comments;

    @OneToMany(mappedBy = "user")
    private List<Bookmark> bookmarks;

    @OneToMany(mappedBy = "user")
    private List<Like> likes;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private ReporterProfile reporterProfile;

    @OneToMany(mappedBy = "reporter")
    private List<Article> articles;

    @OneToMany(mappedBy = "user")
    private List<Subscription> subscriptions;

    @OneToMany(mappedBy = "user")
    private List<Notification> notifications;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        return authorities;
    }
    @Override
    public String getUsername() {
        return email;
    }
}