package thesis.webcryptoexchange.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.core.env.Environment;

import java.util.*;

import thesis.webcryptoexchange.model.Transaction;
import thesis.webcryptoexchange.service.CryptoService;

@Controller
public class CryptoController {
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

    @GetMapping("/new")
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
    public String transactionTrade(Transaction trans, @RequestParam String crypto, @RequestParam Boolean buying, Model model) {
        crypService.transaction(trans, crypto, buying);
        return "redirect:/";
    }

    @GetMapping("/prediction")
    public String predictFuture(@RequestParam String curr, Model model) {
        System.out.println(curr);
        model.addAttribute("curr", curr);
        return "prediction";
    }    
}
