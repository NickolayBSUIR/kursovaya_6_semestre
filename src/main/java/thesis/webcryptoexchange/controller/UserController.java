package thesis.webcryptoexchange.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.HttpSession;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionInformation;
import java.util.List;

import thesis.webcryptoexchange.model.User;
import thesis.webcryptoexchange.service.UserService;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    HttpSession session;
    @Autowired
    SessionRegistry sessionRegistry;

    @GetMapping("/login")
    public String login(User user, Model model) {
        return "login";
    }

    @GetMapping("/login-error")
    public String errorLogin(User user,  RedirectAttributes redirect) {
        String msg = ((AuthenticationException)session.getAttribute("SPRING_SECURITY_LAST_EXCEPTION")).getMessage();
        
        if ("Bad credentials".equals(msg)) {
            msg = "Введён неправильный пароль или логин.";
        }
        else if ("User is disabled".equals(msg)) {
            msg = "Кое-кого заблокировали. По этому поводу обращайтесь к администрации.";
        }

        redirect.addFlashAttribute("msg", msg);
        return "redirect:/login";
    }

    @GetMapping("/reg")
    public String registrationPage(Model model) {
        return "reg";
    }

    @PostMapping("/reg")
    public String registration(User user, Model model, RedirectAttributes redirect) {
        if (!userService.save(user)){
            model.addAttribute("msg", "Пользователь с таким логином уже существует.");
            return "reg";
        }
        redirect.addFlashAttribute("msg", "Новый пользователь зарегистрирован.");
        return "redirect:/login";
    }

    @GetMapping("/block")
    public String blockUser(@RequestParam String user) {
        List<Object> users = sessionRegistry.getAllPrincipals();
		for (Object us: users) {
			List<SessionInformation> sessions = sessionRegistry.getAllSessions(us, false);
			for(SessionInformation sess : sessions) {
				if(((UserDetails)sess.getPrincipal()).getUsername().equals(user)) {;
					sess.expireNow();
				}
			}
        }
		userService.changeEnabling(user, false);
        return "redirect:/users";
    }

    @GetMapping("/unblock")
    public String unblockUser(@RequestParam String user) {
		userService.changeEnabling(user, true);
        return "redirect:/users";
    }

    @GetMapping("/account")
    public String wallet(Model model) {
        model.addAttribute("money", userService.findOneMoney());
        model.addAttribute("currs", userService.findCurrs());
        return "account";
    }

    @GetMapping("/users")
    public String usersView(Model model) {
        model.addAttribute("users", userService.findAll());
        return "users";
    }
}