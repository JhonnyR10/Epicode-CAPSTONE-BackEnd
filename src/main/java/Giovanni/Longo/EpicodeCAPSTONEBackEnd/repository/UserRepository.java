package Giovanni.Longo.EpicodeCAPSTONEBackEnd.repository;

import Giovanni.Longo.EpicodeCAPSTONEBackEnd.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
