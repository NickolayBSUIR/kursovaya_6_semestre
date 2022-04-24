
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

import thesis.webcryptoexchange.model.Currency;
import thesis.webcryptoexchange.repository.CurrencyRepository;

@Service
public class CurrencyService {
    @Autowired
    private CurrencyRepository repo;

    public List<Currency> findAll() {
        return repo.findAll();
    }

    public Thread update(Environment env) {
        Thread thread = new Thread() {
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
                        JSONArray jsa = (JSONArray) job.get("data");

                        List<String> list = new ArrayList<>();

                        for (int i = 0; i < jsa.size(); i++) {
                            String name = "";
                            job = (JSONObject) jsa.get(i);
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
                                Thread.sleep(10000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        return thread;
    }
}
