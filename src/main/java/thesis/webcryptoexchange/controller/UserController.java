package thesis.webcryptoexchange.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import thesis.webcryptoexchange.model.User;
import thesis.webcryptoexchange.service.UserService;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }

    @GetMapping("/reg")
    public String registrationPage(Model model) {
        return "reg";
    }

    @PostMapping("/reg")
    public String registration(User user, Model model) {
        if (!userService.save(user)){
            model.addAttribute("msg", "Пользователь с таким email уже существует");
            return "reg";
        }
        return "redirect:/login";
    }
}