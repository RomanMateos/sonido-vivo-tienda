package cl.duoc.blog.repository;

import cl.duoc.blog.model.Blog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlogRepository extends JpaRepository<Blog, Long> {

    List<Blog> findAllByOrderByFechaDesc();

    List<Blog> findByTituloContainingIgnoreCaseOrderByFechaDesc(String titulo);
}