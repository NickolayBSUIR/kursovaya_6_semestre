package thesis.webcryptoexchange.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.*;

import java.time.*;
import java.util.*;

import thesis.webcryptoexchange.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Transaction findByTime(LocalTime time);

    @Transactional
    @Modifying
    @Query(value = "select * from transactions where user_id = ?1", nativeQuery = true)
    List<Transaction> findByUser(Long val1);
}