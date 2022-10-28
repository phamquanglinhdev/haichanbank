package com.adonis.haichanbank.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class AuthController {
    @GetMapping("login")
    public String Login(@RequestParam(required = false) String error, Model model) {
        if (error != null) {
            switch (error) {
                case "wrong.email":
                    model.addAttribute("message", "Email không tồn tại");
                    break;
                case "wrong.password":
                    model.addAttribute("message", "Sai mật khẩu");
                    break;
                default:
                    model.addAttribute("message", "Lỗi gì đó tôi cũng không biết");
            }
        }
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login";
    }
}
