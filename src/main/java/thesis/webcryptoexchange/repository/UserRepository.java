package thesis.webcryptoexchange.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.*;

import thesis.webcryptoexchange.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByName(String name);
    List<User> findByPassword(String password);

    @Transactional
    @Modifying
    @Query(value = "?1", nativeQuery = true)
    void pureQuery(String str);

    @Transactional
    @Modifying
    @Query(value = "update users set enabled = ?2 where name = ?1", nativeQuery = true)
    void changeEnabling(String name, Boolean enabled);
}