package com.adonis.haichanbank.controller;

import com.adonis.haichanbank.models.History;
import com.adonis.haichanbank.models.Notification;
import com.adonis.haichanbank.models.User;
import com.adonis.haichanbank.repositories.HistoryRepository;
import com.adonis.haichanbank.repositories.NotificationRepository;
import com.adonis.haichanbank.services.UserPrincipal;
import com.adonis.haichanbank.services.UserServicesImpl;
import com.adonis.haichanbank.utils.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("bank")
public class HomeController {
    @Autowired
    UserServicesImpl userServices;

    @Autowired
    CurrentUser currentUser;

    @Autowired
    HistoryRepository historyRepository;

    @GetMapping(value = {"", "/index"})
    public String Home(Model model) {
//        System.out.println(currentUser.get());
        model.addAttribute("currentUser", currentUser.get());
        model.addAttribute("allUser", userServices.getAllUsers());
        model.addAttribute("history", new History());
        return "index";
    }

    @GetMapping("wallet")
    public String Wallet(Model model) {
        model.addAttribute("currentUser", currentUser.get());
        model.addAttribute("histories", historyRepository.getHistoriesByFromOrToOrderByCreatedDesc(currentUser.get(), currentUser.get()));
        return "wallet";
    }

    @Autowired
    NotificationRepository notificationRepository;

    @GetMapping("notifications")
    public String Notifications(Model model) {
        model.addAttribute("currentUser", currentUser.get());
        List<Notification> notifications = notificationRepository.findByUserOrderByCreatedDesc(currentUser.get());
        model.addAttribute("notifications", notifications);
        return "notifications";
    }
}
