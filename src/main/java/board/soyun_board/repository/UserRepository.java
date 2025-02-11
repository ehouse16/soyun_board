package board.soyun_board.repository;

import board.soyun_board.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

   User findById(long id);
}
