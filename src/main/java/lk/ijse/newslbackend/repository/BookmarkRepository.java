package lk.ijse.newslbackend.repository;

import lk.ijse.newslbackend.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    @Query("SELECT b FROM Bookmark b WHERE b.user.username = :username")
    List<Bookmark> findAllByUsername(String username);
}
