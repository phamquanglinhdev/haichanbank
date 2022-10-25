package com.adonis.haichanbank.controller;

import com.adonis.haichanbank.models.History;
import com.adonis.haichanbank.models.User;
import com.adonis.haichanbank.repositories.HistoryRepository;
import com.adonis.haichanbank.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("bank")
public class MoneyController {
    @Autowired
    HistoryRepository historyRepository;

    @Autowired
    UserRepository userRepository;

    @PostMapping("transfer")
    public String TransferMoney(@ModelAttribute History history, Model model, Principal principal) {
        User fromUser = userRepository.findByEmail(principal.getName());
        history.setFrom(fromUser);
        User toUser = history.getTo();
        int amount = history.getAmount();
        fromUser.setAmount(fromUser.getAmount() - amount);
        toUser.setAmount(toUser.getAmount() + amount);
        userRepository.save(fromUser);
        userRepository.save(toUser);
        historyRepository.save(history);
        return "redirect:/bank";
    }
}
