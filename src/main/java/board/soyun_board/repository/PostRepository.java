package board.soyun_board.repository;


import board.soyun_board.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByTitleContaining(String keyword);
    List<Post> findAllByContentContaining(String keyword);
}
