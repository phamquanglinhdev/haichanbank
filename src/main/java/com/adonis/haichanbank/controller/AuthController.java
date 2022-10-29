package com.adonis.haichanbank.controller;

import com.adonis.haichanbank.models.Notification;
import com.adonis.haichanbank.models.OTP;
import com.adonis.haichanbank.models.User;
import com.adonis.haichanbank.repositories.NotificationRepository;
import com.adonis.haichanbank.repositories.OTPRepository;
import com.adonis.haichanbank.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.Random;

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

    @GetMapping("register")
    public String register() {
        return "register";
    }

    @Autowired
    OTPRepository otpRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    NotificationRepository notificationRepository;

    @PostMapping("create-user")
    public String create(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String password,
            @RequestParam(required = false) String confirm,
            @RequestParam(required = false) String otp,
            RedirectAttributes model
    ) {
        if (name == null || email == null || phone == null || password == null || confirm == null || otp == null) {
            model.addFlashAttribute("error", "Thiếu thông tin");
            return "redirect:/register";
        }
        if (!password.equals(confirm)) {
            model.addFlashAttribute("error", "Nhập lại mật khẩu không đúng");
            return "redirect:/register";
        }
        OTP otp_contain = otpRepository.findByOtp(otp);
        if (otp_contain == null) {
            model.addFlashAttribute("error", "OTP Không chính xác");
            return "redirect:/register";
        }
        if (!Objects.equals(otp_contain.getPhone(), phone)) {
            model.addFlashAttribute("error", "OTP Không chính xác");
            otpRepository.delete(otp_contain);
            return "redirect:/register";
        }
        otp_contain.setUsed(true);
        otpRepository.save(otp_contain);
        User existUser = userRepository.findByEmail(email);
        if (existUser != null) {
            model.addFlashAttribute("error", "Email đã tồn tại");
            return "redirect:/register";
        }
        User user = new User();
        user.setRole("user");
        user.setAvatar("https://i.vietgiaitri.com/2019/9/10/iu-em-gai-quoc-dan-cua-han-quoc-la-ai-de9c2e.jpg");
        user.setName(name);
        user.setEmail(email);
        Random gen = new Random();
        int LastDigest = gen.nextInt((9999 - 1111) + 1) + 1111;
        int midDigest = gen.nextInt((9999 - 1111) + 1) + 1111;
        user.setCard("9704 1801 " + midDigest + " " + LastDigest);
        user.setAmount(100000);
        user.setPhone(phone);
        user.setRemember("null");
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        user = userRepository.findByEmail(email);
        Notification notification = new Notification();
        notification.setTitle("Quà tặng cho người mới");
        notification.setUser(user);
        notification.setMessage("Chào mừng " + name + ",tặng bạn 100.000 đ để trải nghiệm ");
        notificationRepository.save(notification);
        model.addFlashAttribute("success", "Đăng ký thành công,đăng nhập đi's");
        return "redirect:/login";
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
