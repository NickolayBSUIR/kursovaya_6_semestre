package thesis.webcryptoexchange;

import org.junit.runner.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.test.context.junit4.*;
import org.springframework.test.context.*;
import org.springframework.boot.test.autoconfigure.orm.jpa.*;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.*;

import thesis.webcryptoexchange.repository.*;
import thesis.webcryptoexchange.model.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
class WebCryptoExchangeApplicationTests {
	@Autowired
    private CurrencyRepository currRepo;
    @Autowired
    private TransactionRepository tranRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
	private UserCurrencyRepository uscrRepo;

    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    // User
	@Test
    public void shouldSaveUser() {
        User user = new User();
        user.setName("test");
        User saved = userRepo.save(user);
        assertThat(saved).usingRecursiveComparison().ignoringFields("userId").isEqualTo(user);
    }

    @Test
    public void shouldDeleteUser() {
        User user = new User();
        user.setName("test");
        User saved = userRepo.save(user);
        userRepo.delete(saved);
        assertEquals(null, userRepo.findByName("test"));
    }

    @Test
    public void isNameUniqueUser() {
        User user = new User();
        user.setName("test");
        userRepo.save(user);
        final User otherUser = new User();
        otherUser.setName("test");
        assertThrows(Exception.class, () -> userRepo.save(otherUser));
    }

    @Test
    public void canFindAllUser() {
        User user = new User();
        user.setName("test1");
        userRepo.save(user);
        user = new User();
        user.setName("test2");
        userRepo.save(user);
        user = new User();
        user.setName("test3");
        userRepo.save(user);
        assertEquals(3, userRepo.findAll().size());
    }

    @Test
    public void canFindUserByPassword() {
        User user = new User();
        user.setPassword("testpass");
        userRepo.save(user);
        user = new User();
        user.setPassword("testpass1");
        userRepo.save(user);
        user = new User();
        user.setPassword("testpass1");
        userRepo.save(user);
        assertEquals(2, userRepo.findByPassword("testpass1").size());
    }

    @Test
    public void canDisableUser() {
        User user = new User();
        user.setName("test1");
        user = userRepo.save(user);
        user.setEnabled(false);
        user = userRepo.save(user);
        assertEquals(false, user.getEnabled());
    }

    @Test
    public void isUserPasswordCrypted() {
        User user = new User();
        String pass = "test1";
        user.setPassword(bCryptPasswordEncoder.encode(pass));
        user = userRepo.save(user);
        assertTrue(pass != user.getPassword() && bCryptPasswordEncoder.matches(pass, user.getPassword()));
    }

    // Currency
    @Test
    public void shouldSaveCurrency() {
        Currency curr = new Currency();
        curr.setName("BTC");
        Currency saved = currRepo.save(curr);
        assertThat(saved).usingRecursiveComparison().ignoringFields("currencyId").isEqualTo(curr);
    }

    @Test
    public void shouldDeleteCurrency() {
        Currency curr = new Currency();
        curr.setName("BTC");
        Currency saved = currRepo.save(curr);
        currRepo.delete(saved);
        assertEquals(null, userRepo.findByName("BTC"));
    }

    @Test
    public void isNameUniqueCurrency() {
        Currency curr = new Currency();
        curr.setName("BTC");
        currRepo.save(curr);
        final Currency otherCurr = new Currency();
        otherCurr.setName("BTC");
        assertThrows(Exception.class, () -> currRepo.save(otherCurr));
    }

    @Test
    public void canFindCurrencyByRate() {
        Currency curr = new Currency();
        curr.setRate(1.0);
        currRepo.save(curr);
        curr = new Currency();
        curr.setRate(1.1);
        currRepo.save(curr);
        curr = new Currency();
        curr.setRate(1.1);
        currRepo.save(curr);
        assertEquals(2, currRepo.findByRate(1.1).size());
    }

    @Test
    public void canUpdateCurrency() {
        Currency curr = new Currency();
        curr.setName("BTC");
        curr.setRate(1.0);
        curr = currRepo.save(curr);
        curr.setRate(1.1);
        currRepo.save(curr);
        assertEquals((Double)1.1, currRepo.findByName("BTC").getRate());
    }
    
    // UserCurrency
    @Test
    public void shouldSaveUserCurrency() {
        UserCurrency uscr = new UserCurrency();
        Currency curr = new Currency();
        User user = new User();
        curr = currRepo.save(curr);
        user = userRepo.save(user);
        uscr.setUser(user);
        uscr.setCurrency(curr);
        UserCurrency saved = uscrRepo.save(uscr);
        assertThat(saved).usingRecursiveComparison().ignoringFields("user_currencyId").isEqualTo(uscr);
    }

    @Test
    public void shouldDeleteUserCurrency() {
        UserCurrency uscr = new UserCurrency();
        Currency curr = new Currency();
        User user = new User();
        curr = currRepo.save(curr);
        user = userRepo.save(user);
        uscr.setUser(user);
        uscr.setCurrency(curr);
        UserCurrency saved = uscrRepo.save(uscr);
        Long id = saved.getId();
        uscrRepo.delete(saved);
        assertEquals(false, uscrRepo.findById(id).isPresent());
    }

    @Test
    public void canFindUserCurrencyAll() {
        UserCurrency uscr = new UserCurrency();
        Currency curr = new Currency();
        User user = new User();
        curr = currRepo.save(curr);
        user = userRepo.save(user);
        uscr.setUser(user);
        uscr.setCurrency(curr);
        uscrRepo.save(uscr);
        uscr = new UserCurrency();
        curr = new Currency();
        user = new User();
        curr = currRepo.save(curr);
        user = userRepo.save(user);
        uscr.setUser(user);
        uscr.setCurrency(curr);
        uscrRepo.save(uscr);
        assertEquals(2, uscrRepo.findAll().size());
    }

    @Test
    public void canFindUserCurrencyByUser() {
        UserCurrency uscr = new UserCurrency();
        Currency curr = new Currency();
        User user = new User();
        curr = currRepo.save(curr);
        user = userRepo.save(user);
        uscr.setUser(user);
        uscr.setCurrency(curr);
        uscrRepo.save(uscr);
        uscr = new UserCurrency();
        curr = new Currency();
        curr = currRepo.save(curr);
        uscr.setCurrency(curr);
        uscr.setUser(user);
        uscrRepo.save(uscr);
        assertEquals(2, uscrRepo.findByUser(user.getId()).size());
    }

    @Test
    public void canDeleteUserCurrencyByUserDeletion() {
        UserCurrency uscr = new UserCurrency();
        Currency curr = new Currency();
        User user = new User();
        curr = currRepo.save(curr);
        user = userRepo.save(user);
        uscr.setUser(user);
        uscr.setCurrency(curr);
        uscr = uscrRepo.save(uscr);
        user = uscr.getUser();
        Long id = uscr.getId();
        userRepo.delete(user);
        assertEquals(0, uscrRepo.findByUser(id).size());
    }

    @Test
    public void canDeleteUserCurrencyByCurrencyDeletion() {
        UserCurrency uscr = new UserCurrency();
        Currency curr = new Currency();
        User user = new User();
        curr = currRepo.save(curr);
        user = userRepo.save(user);
        uscr.setUser(user);
        uscr.setCurrency(curr);
        uscr = uscrRepo.save(uscr);
        curr = uscr.getCurrency();
        Long id = curr.getId();
        currRepo.delete(curr);
        assertEquals(0, uscrRepo.findByCurrency(id).size());
    }

    @Test
    public void canCountAllUserAssets() {
        UserCurrency uscr = new UserCurrency();
        Currency curr = new Currency();
        User user = new User();
        Double final_assets = 0.0;
        user.setMoney(1000.0);
        final_assets += 1000;
        curr.setRate(20.0);
        curr = currRepo.save(curr);
        user = userRepo.save(user);
        uscr.setCount(50.0);
        uscr.setUser(user);
        uscr.setCurrency(curr);
        final_assets += uscr.getCount() * uscr.getCurrency().getRate();
        uscrRepo.save(uscr);
        uscr = new UserCurrency();
        curr = new Currency();
        curr.setRate(40.0);
        curr = currRepo.save(curr);
        uscr.setCount(50.0);
        uscr.setCurrency(curr);
        uscr.setUser(user);
        final_assets += uscr.getCount() * uscr.getCurrency().getRate();
        uscr = uscrRepo.save(uscr);
        assertEquals((Double)4000.0, final_assets);
    }

    //Transaction
    @Test
    public void shouldSaveTransaction() {
        Transaction tran = new Transaction();
        Currency curr = new Currency();
        User user = new User();
        curr = currRepo.save(curr);
        user = userRepo.save(user);
        tran.setUser(user);
        tran.setCurrency(curr);
        Transaction saved = tranRepo.save(tran);
        assertThat(saved).usingRecursiveComparison().ignoringFields("transactionId").isEqualTo(tran);
    }

    @Test
    public void shouldDeleteTransaction() {
        Transaction tran = new Transaction();
        Currency curr = new Currency();
        User user = new User();
        curr = currRepo.save(curr);
        user = userRepo.save(user);
        tran.setUser(user);
        tran.setCurrency(curr);
        Transaction saved = tranRepo.save(tran);
        Long id = saved.getId();
        tranRepo.delete(tran);
        assertEquals(false, tranRepo.findById(id).isPresent());
    }

    @Test
    public void canFindTransactionAll() {
        Transaction tran = new Transaction();
        Currency curr = new Currency();
        User user = new User();
        curr = currRepo.save(curr);
        user = userRepo.save(user);
        tran.setUser(user);
        tran.setCurrency(curr);
        tranRepo.save(tran);
        tran = new Transaction();
        curr = new Currency();
        user = new User();
        curr = currRepo.save(curr);
        user = userRepo.save(user);
        tran.setUser(user);
        tran.setCurrency(curr);
        tranRepo.save(tran);
        assertEquals(2, tranRepo.findAll().size());
    }

    @Test
    public void canFindTransactionByUser() {
        Transaction tran = new Transaction();
        Currency curr = new Currency();
        User user = new User();
        curr = currRepo.save(curr);
        user = userRepo.save(user);
        tran.setUser(user);
        tran.setCurrency(curr);
        tranRepo.save(tran);
        tran = new Transaction();
        curr = new Currency();
        curr = currRepo.save(curr);
        tran.setCurrency(curr);
        tran.setUser(user);
        tranRepo.save(tran);
        assertEquals(2, tranRepo.findByUser(user.getId()).size());
    }

    @Test
    public void canDeleteTransactionByUserDeletion() {
        Transaction tran = new Transaction();
        Currency curr = new Currency();
        User user = new User();
        curr = currRepo.save(curr);
        user = userRepo.save(user);
        tran.setUser(user);
        tran.setCurrency(curr);
        tran = tranRepo.save(tran);
        user = tran.getUser();
        Long id = tran.getId();
        userRepo.delete(user);
        assertEquals(0, tranRepo.findByUser(id).size());
    }

    @Test
    public void canDeleteTransactionByCurrencyDeletion() {
        Transaction tran = new Transaction();
        Currency curr = new Currency();
        User user = new User();
        curr = currRepo.save(curr);
        user = userRepo.save(user);
        tran.setUser(user);
        tran.setCurrency(curr);
        tran = tranRepo.save(tran);
        curr = tran.getCurrency();
        Long id = curr.getId();
        currRepo.delete(curr);
        assertEquals(0, tranRepo.findByCurrency(id).size());
    }

    @Test
    public void canCountAllBuyingAssets() {
        Transaction tran = new Transaction();
        Currency curr = new Currency();
        User user = new User();
        Double final_assets = 0.0;
        curr.setRate(20.0);
        curr = currRepo.save(curr);
        user = userRepo.save(user);
        tran.setCurrencyCount(50.0);
        tran.setUser(user);
        tran.setCurrency(curr);
        final_assets += tran.getCurrencyCount() * tran.getCurrency().getRate();
        tranRepo.save(tran);
        tran = new Transaction();
        curr = new Currency();
        curr.setRate(40.0);
        curr = currRepo.save(curr);
        tran.setCurrencyCount(50.0);
        tran.setCurrency(curr);
        tran.setUser(user);
        final_assets += tran.getCurrencyCount() * tran.getCurrency().getRate();
        tran = tranRepo.save(tran);
        assertEquals((Double)3000.0, final_assets);
    }
}