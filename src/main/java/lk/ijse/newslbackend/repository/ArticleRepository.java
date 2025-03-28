package lk.ijse.newslbackend.repository;

import lk.ijse.newslbackend.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article,String> {
}
