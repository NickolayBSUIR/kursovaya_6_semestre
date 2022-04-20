package thesis.webcryptoexchange.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.core.env.Environment;

import thesis.webcryptoexchange.model.Currency;
import thesis.webcryptoexchange.service.CurrencyService;

@Controller
public class WebUserController {
    @Autowired
    private CurrencyService currService;

    @Autowired
    private Environment env;

    private Boolean isUpdate = false;
    private Thread updateThread = new Thread();

    @GetMapping("/mah")
    public String login(Model model, String error) {
        model.addAttribute("currs", currService.findAll());
        model.addAttribute("col", isUpdate);
        return "crypto";
    }

    @GetMapping("/new")
    public String juh(Model model, String error) {
        if (!isUpdate) {
            updateThread = currService.update(env);
            updateThread.start();
            isUpdate = true;
        }
        else {
            updateThread.stop();
            isUpdate = false;
        }
        return "redirect:/mah";
    }
}
