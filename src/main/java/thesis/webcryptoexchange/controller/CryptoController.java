package thesis.webcryptoexchange.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

import thesis.webcryptoexchange.model.Transaction;
import thesis.webcryptoexchange.service.CryptoService;

@Controller
public class CryptoController {
    // private Map<String,Model> redirectModels = new HashMap<String,Model>();

    @Autowired
    private CryptoService crypService;

    @Autowired
    private Environment env;

    private Boolean isUpdate = false;
    private Thread updateThread = new Thread();

    @GetMapping("/")
    public String cryptoExchange(Model model, String error) {
        model.addAttribute("currs", crypService.findAll());
        model.addAttribute("col", isUpdate);
        model.addAttribute("btn", (isUpdate ? "btn btn-danger" : "btn btn-success"));
        return "crypto";
    }

    @GetMapping("/update")
    public String updateControl(Model model, String error) {
        if (!isUpdate) {
            updateThread = crypService.update(env);
            updateThread.start();
            isUpdate = true;
        }
        else {
            updateThread.stop();
            isUpdate = false;
        }
        return "redirect:/";
    }

    @PostMapping("/transac")
    public String transactionTrade(Transaction trans, @RequestParam String crypto, @RequestParam Boolean buying, RedirectAttributes redirect) {
        if (crypService.transaction(trans, crypto, buying)){
            redirect.addFlashAttribute("msg", "Транзакция проведена успешно!");
        }
        else {
            redirect.addFlashAttribute("msg", "Транзакция безуспешна.");
        }
        return "redirect:/";
    }

    @GetMapping("/prediction")
    public String predictFuture(@RequestParam String curr, Model model) {
        model.addAttribute("curr", curr);
        return "prediction";
    }

    @GetMapping("/transacs")
    public String transacsView(Model model) {
        model.addAttribute("trans", crypService.findTransacs());
        return "transacs";
    }
}
