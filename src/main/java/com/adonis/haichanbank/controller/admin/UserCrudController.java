package com.adonis.haichanbank.controller.admin;

import com.adonis.haichanbank.models.Notification;
import com.adonis.haichanbank.models.User;
import com.adonis.haichanbank.repositories.NotificationRepository;
import com.adonis.haichanbank.repositories.UserRepository;
import com.adonis.haichanbank.utils.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("admin/user")
public class UserCrudController {
    @Autowired
    CurrentUser currentUser;

    @Autowired
    UserRepository userRepository;

    @GetMapping(value = {"", "index"})
    public String index(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        model.addAttribute("currentUser", currentUser.get());
        return "admin/user/index";
    }


    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") int id, Model model) {
        model.addAttribute("currentUser", currentUser.get());
        User user = userRepository.getReferenceById((long) id);
        if (user == null) {
            model.addAttribute("error", "Không tìm thấy tài khoản");
            return "redirect:/admin/user/index";
        }
        model.addAttribute("user", user);
        return "admin/user/edit";
    }

    @PostMapping("/update/{id}")
    public String update(
            @PathVariable("id") int id,
            RedirectAttributes model,
            @RequestParam String role
    ) {
        String[] nameRoles = new String[]{"admin", "user", "business"};
        List<String> roles = new ArrayList<>(Arrays.asList(nameRoles));
        if (!roles.contains(role)) {
            model.addFlashAttribute("error", "Đã xảy ra lỗi. Phân quyền không tồn tại");
            return "redirect:/admin/user/index";
        }
        User user = userRepository.getReferenceById((long) id);
        if (user == null) {
            model.addFlashAttribute("error", "Không tìm thấy tài khoản");
            return "redirect:/admin/user/index";
        }
        user.setRole(role);
        userRepository.save(user);
        model.addFlashAttribute("success", "Cập nhật phân quyền thành công");
        return "redirect:/admin/user/index";
    }

    @GetMapping("notification/{id}")
    public String notification(@PathVariable("id") Long id, Model model) {
        model.addAttribute("currentUser", currentUser.get());
        User user = userRepository.getReferenceById(id);
        if (user == null) {
            model.addAttribute("error", "Không tìm thấy tài khoản");
            return "redirect:/admin/user/index";
        }
        model.addAttribute("user", user);
        return "admin/user/notification";
    }

    @Autowired
    NotificationRepository notificationRepository;

    @PostMapping("send/{id}")
    public String send(
            @PathVariable("id") Long id,
            RedirectAttributes model,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String message
    ) {
        if (title == null || message == null) {
            model.addFlashAttribute("error", "Tin nhắn hoặc tiêu đề không được phép trống !");
            return "redirect:/admin/user/notification/" + id;
        }
        User user = userRepository.getReferenceById(id);
        if (user == null) {
            model.addFlashAttribute("error", "Không tìm thấy tài khoản");
            return "redirect:/admin/user/index";
        }
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessage(message);
        notification.setTitle(title);
        notificationRepository.save(notification);
        model.addFlashAttribute("success", "Gửi đi thành công");
        return "redirect:/admin/user/index";
    }
}
