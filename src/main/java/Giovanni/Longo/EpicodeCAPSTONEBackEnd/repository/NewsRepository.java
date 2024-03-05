package Giovanni.Longo.EpicodeCAPSTONEBackEnd.repository;

import Giovanni.Longo.EpicodeCAPSTONEBackEnd.model.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NewsRepository extends JpaRepository<News, Long> {
    @Query("SELECT n FROM News n WHERE LOWER(n.title) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<News> findByTitleContainingIgnoreCase(@Param("keyword") String keyword);
}
