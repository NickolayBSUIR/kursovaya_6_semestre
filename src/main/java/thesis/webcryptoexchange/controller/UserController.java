package thesis.webcryptoexchange.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
        redirect.addFlashAttribute("msg", (String)((AuthenticationException)session.getAttribute("SPRING_SECURITY_LAST_EXCEPTION")).getMessage());
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
		userService.blockUser(user);
        return "users";
    }

    @GetMapping("/account")
    public String wallet(Model model) {
        model.addAttribute("money", userService.findOneMoney());
        model.addAttribute("currs", userService.findCurrs());
        return "account";
    }
}