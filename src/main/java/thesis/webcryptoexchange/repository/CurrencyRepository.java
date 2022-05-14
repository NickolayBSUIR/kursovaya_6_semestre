package thesis.webcryptoexchange.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.*;

import thesis.webcryptoexchange.model.Currency;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {
    Currency findByName(String name);
    List<Currency> findByRate(Double rate);

    @Transactional
    @Modifying
    @Query(value = "?1", nativeQuery = true)
    void pureQuery(String str);
}