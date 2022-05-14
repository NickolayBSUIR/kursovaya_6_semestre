
package thesis.webcryptoexchange.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.core.env.Environment;

import java.net.*;
import java.util.Scanner;
import java.sql.*;
import org.json.simple.*;
import org.json.simple.parser.*;
import javax.servlet.http.HttpSession;

import thesis.webcryptoexchange.model.Currency;
import thesis.webcryptoexchange.model.UserCurrency;
import thesis.webcryptoexchange.model.Transaction;
import thesis.webcryptoexchange.model.User;
import thesis.webcryptoexchange.repository.CurrencyRepository;
import thesis.webcryptoexchange.repository.UserCurrencyRepository;
import thesis.webcryptoexchange.repository.TransactionRepository;
import thesis.webcryptoexchange.repository.UserRepository;
import thesis.webcryptoexchange.pattern.Singleton;
import thesis.webcryptoexchange.pattern.Iterator;
import thesis.webcryptoexchange.pattern.JSONCollection;

@Service
public class CryptoService {
    @Autowired
    private CurrencyRepository currRepo;
    @Autowired
    private TransactionRepository tranRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private UserCurrencyRepository uscrRepo;

    @Autowired
    private HttpSession session;

    private Thread mainThread = new Thread();

    public List<Currency> findAll() {
        return currRepo.findAll();
    }

    public List<Transaction> findTransacs() {
        return tranRepo.findAll();
    }

    public List<Transaction> findUserTransacs() {
        return tranRepo.findByUser(userRepo.findByName((String)session.getAttribute("user")).getId());
    }

    public void update(Environment env) {
        if (Singleton.getInstance(false).value) {
            mainThread = new Thread() {
                public void run() {
                    while (true) {
                        try {
                            URL url = new URL(
                                    "https://data.messari.io/api/v2/assets?fields=symbol,name,metrics/market_data&limit=500");
                            String resp = "";
                            Scanner sc = new Scanner(url.openStream());
    
                            while (sc.hasNext()) {
                                resp += sc.nextLine();
                            }
    
                            sc.close();
    
                            JSONParser parser = new JSONParser();
                            JSONObject job = (JSONObject) parser.parse(resp);
                            Iterator jsonIt = new JSONCollection((JSONArray) job.get("data")).iterator();
    
                            List<String> list = new ArrayList<>();
    
                            while (jsonIt.hasNext()) {
                                String name = "";
                                job = (JSONObject)jsonIt.next();
                                if (job.get("symbol") == null) {
                                    name = job.get("name").toString();
                                } else {
                                    name = job.get("symbol").toString();
                                }
                                job = (JSONObject) job.get("metrics");
                                job = (JSONObject) job.get("market_data");
                                if (job.get("price_usd") == null) {
                                    continue;
                                }
                                String rate = job.get("price_usd").toString();
                                if (job.get("percent_change_usd_last_24_hours") == null || job.get("percent_change_usd_last_1_hour") == null) {
                                    continue;
                                }
                                String change_hour = job.get("percent_change_usd_last_1_hour").toString();
                                String change_day = job.get("percent_change_usd_last_24_hours").toString();
                                list.add("('" + name + "'," + rate + "," + change_hour + "," + change_day + ")");
                            }
    
                            Statement statement = (Statement) DriverManager.getConnection(
                                    env.getRequiredProperty("jdbc.url"), env.getRequiredProperty("jdbc.username"),
                                    env.getRequiredProperty("jdbc.password")).createStatement();
                            statement.executeUpdate("INSERT currencies(name, rate, change_hour, change_day) VALUES "
                                    + String.join(", ", list)
                                    + " ON DUPLICATE KEY UPDATE rate = VALUES(rate), change_hour = VALUES(change_hour), change_day = VALUES(change_day)");
                                    Thread.sleep(300000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            mainThread.start();
        }
        else {
            mainThread.stop();
        }
    }

    public Boolean transaction(Transaction trans, String currency, Boolean buying) {
        User user = userRepo.findByName((String)session.getAttribute("user"));
        Currency curr = currRepo.findByName(currency);
        UserCurrency uscr = new UserCurrency();
        Boolean delete = false;
        if (uscrRepo.findByThem(user.getId(), curr.getId()).size() == 0) {
            uscr.setUser(user);
            uscr.setCurrency(curr);
        }
        else {
            uscr = uscrRepo.findByThem(user.getId(), curr.getId()).get(0);
        }
        user.getTransactions().add(trans);
        curr.getTransactions().add(trans);
        trans.setCurrency(curr);
        trans.setUser(user);
        if (buying) {
            if ((curr.getRate() * trans.getCurrencyCount()) < user.getMoney()) {
                uscr.setCount(uscr.getCount() + trans.getCurrencyCount());
                trans.setUsdCount(curr.getRate() * trans.getCurrencyCount());
                user.setMoney(user.getMoney() - curr.getRate() * trans.getCurrencyCount());
            }
            else if ((curr.getRate() * trans.getCurrencyCount()) < user.getMoney() * 1.1) {
                trans.setUsdCount(user.getMoney());
                trans.setCurrencyCount(user.getMoney() / curr.getRate());
                uscr.setCount(uscr.getCount() + trans.getCurrencyCount());
                user.setMoney(0.0);
            }
            else {
                return false;
            }
        }
        else {
            trans.setBuying(false);
            if ((trans.getUsdCount() / curr.getRate()) < uscr.getCount()) {
                uscr.setCount(uscr.getCount() - trans.getUsdCount() / curr.getRate());
                trans.setCurrencyCount(trans.getUsdCount() / curr.getRate());
                user.setMoney(user.getMoney() + trans.getUsdCount());
            }
            else if ((trans.getUsdCount() / curr.getRate()) < uscr.getCount() * 1.1) {
                trans.setUsdCount(uscr.getCount() * curr.getRate());
                trans.setCurrencyCount(uscr.getCount());
                user.setMoney(user.getMoney() + trans.getUsdCount());
                delete = true;
            }
            else {
                return false;
            }
        }
        user.getTransactions().add(trans);
        curr.getTransactions().add(trans);
        trans.setCurrency(curr);
        trans.setUser(user);
        user.getUserCurrencies().add(uscr);
        curr.getUserCurrencies().add(uscr);
        tranRepo.save(trans);
        if (delete) {
            uscrRepo.delete(uscr);
        }
        else {
            uscrRepo.save(uscr);
        }
        return true;
    }
}