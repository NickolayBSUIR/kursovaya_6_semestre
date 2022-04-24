
package thesis.webcryptoexchange.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.core.env.Environment;

import java.net.*;
import java.util.Scanner;

import thesis.webcryptoexchange.model.User;
import thesis.webcryptoexchange.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository repo;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public List<User> findAll() {
        return repo.findAll();
    }

    public boolean save(User user) {
        if (repo.findByName(user.getName()) != null)
            return false;
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        repo.save(user);
        return true;
    }
}
