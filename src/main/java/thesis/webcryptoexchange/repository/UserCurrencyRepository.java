package thesis.webcryptoexchange.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.*;

import java.util.*;

import thesis.webcryptoexchange.model.UserCurrency;

public interface UserCurrencyRepository extends JpaRepository<UserCurrency, Long> {
    @Transactional
    @Modifying
    @Query(value = "select * from user_currencies where user_id = ?1 and currency_id = ?2", nativeQuery = true)
    List<UserCurrency> findByThem(Long val1, Long val2);

    @Transactional
    @Modifying
    @Query(value = "select * from user_currencies where user_id = ?1", nativeQuery = true)
    List<UserCurrency> findByUser(Long val1);
}