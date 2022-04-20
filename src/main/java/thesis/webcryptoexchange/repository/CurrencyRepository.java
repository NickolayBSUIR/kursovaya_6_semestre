package thesis.webcryptoexchange.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.*;

import thesis.webcryptoexchange.model.Currency;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {
    Currency findByName(String name);

    @Transactional
    @Modifying
    @Query(value = "?1", nativeQuery = true)
    void pureQuery(String str);
}
