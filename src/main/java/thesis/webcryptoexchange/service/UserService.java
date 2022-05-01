
package thesis.webcryptoexchange.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.core.env.Environment;

import java.net.*;
import java.util.Scanner;
import javax.servlet.http.HttpSession;

import thesis.webcryptoexchange.model.User;
import thesis.webcryptoexchange.model.UserCurrency;
import thesis.webcryptoexchange.repository.UserRepository;
import thesis.webcryptoexchange.repository.UserCurrencyRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepo;
     @Autowired
    private UserCurrencyRepository uscrRepo;

    @Autowired
    private HttpSession session;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public List<User> findAll() {
        return userRepo.findAll();
    }

    public Double findOneMoney() {
        return userRepo.findByName((String)session.getAttribute("user")).getMoney();
    }

    public void blockUser(String user) {
        userRepo.blockUser(user);
    }

    public List<UserCurrency> findCurrs() {
        return uscrRepo.findByUser(userRepo.findByName((String)session.getAttribute("user")).getId());
    }

    public boolean save(User user) {
        if (userRepo.findByName(user.getName()) != null)
            return false;
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepo.save(user);
        return true;
    }
}
