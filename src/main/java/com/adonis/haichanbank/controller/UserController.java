package com.adonis.haichanbank.controller;

import com.adonis.haichanbank.models.OTP;
import com.adonis.haichanbank.models.User;
import com.adonis.haichanbank.repositories.OTPRepository;
import com.adonis.haichanbank.repositories.UserRepository;
import com.adonis.haichanbank.services.UserServices;
import com.adonis.haichanbank.utils.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@Controller
@RequestMapping("bank/user")
public class UserController {
    @Autowired
    CurrentUser currentUser;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("")
    public String Index(Model model) {
        model.addAttribute("currentUser", currentUser.get());
        return "user/info";
    }

    @PostMapping("/change-email")
    public String changeEmail(@RequestParam(required = false, defaultValue = "") String email, RedirectAttributes model, HttpServletRequest request, HttpServletResponse response) {
        if (email == null || email.equals("")) {
            model.addFlashAttribute("error", "Email không được để trống");
            return "redirect:/bank/user";
        }
        User existUser = userRepository.findByEmail(email);
        if (existUser != null) {
            model.addFlashAttribute("error", "Email đã tồn tại");
            return "redirect:/bank/user";
        }
        User user = currentUser.get();
        user.setEmail(email);
        userRepository.save(user);
        model.addFlashAttribute("success", "Thành công, vui lòng đăng nhập lại");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login";
    }

    @PostMapping("/change-name")
    public String changeName(@RequestParam(required = false, defaultValue = "") String name, RedirectAttributes model) {
        if (name == null || name.equals("")) {
            model.addFlashAttribute("error", "Tên không được để trống");
            return "redirect:/bank/user";
        }
        User user = currentUser.get();
        user.setName(name);
        userRepository.save(user);
        model.addFlashAttribute("success", "Đổi tên thành công ");
        return "redirect:/bank/user";
    }

    @PostMapping("change-password")
    public String changePassword(
            @RequestParam(required = false, defaultValue = "") String password,
            @RequestParam(required = false, defaultValue = "") String confirm,
            @RequestParam(required = false, defaultValue = "") String old,
            RedirectAttributes model
    ) {
        if (password == null || password.equals("")) {
            model.addFlashAttribute("error", "Mật khẩu không được để trống");
            return "redirect:/bank/user";
        }
        if (!password.equals(confirm)) {
            model.addFlashAttribute("error", "Nhập lại mật khẩu không đúng");
            return "redirect:/bank/user";
        }
        User user = currentUser.get();
        if (!passwordEncoder.matches(old, user.getPassword())) {
            model.addFlashAttribute("error", "Mật khẩu cũ không đúng");
            return "redirect:/bank/user";
        }
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        model.addFlashAttribute("success", "Cập nhật mật khẩu thành công");
        return "redirect:/bank/user";
    }

    @Autowired
    OTPRepository otpRepository;

    @PostMapping("change-phone")
    public String changePhone(
            @RequestParam(required = false, defaultValue = "") String phone,
            @RequestParam(required = false, defaultValue = "") String oldOTP,
            @RequestParam(required = false, defaultValue = "") String newOTP,
            RedirectAttributes model
    ) {
        if (phone == null || phone.equals("")) {
            model.addFlashAttribute("error", "Số điện thoại không được để trống");
            return "redirect:/bank/user";
        }
        OTP newOTPContain = otpRepository.findByOtp(newOTP);
        OTP oldOTPContain = otpRepository.findByOtp(oldOTP);
        if (newOTPContain == null || oldOTPContain == null) {
            model.addFlashAttribute("error", "OTP Lỗi, vui lòng thực hiện lại ");
            return "redirect:/bank/user";
        }
        if (newOTPContain.isUsed() || oldOTPContain.isUsed()) {
            otpRepository.delete(oldOTPContain);
            otpRepository.delete(newOTPContain);
            model.addFlashAttribute("error", "OTP Lỗi, vui lòng thực hiện lại ");
            return "redirect:/bank/user";
        }
        if (!Objects.equals(oldOTPContain.getPhone(), currentUser.get().getPhone())) {
            model.addFlashAttribute("error", "OTP từ SDT Cũ không hợp lệ, vui lòng thực hiện lại");
            otpRepository.delete(oldOTPContain);
            otpRepository.delete(newOTPContain);
            return "redirect:/bank/user";
        }
        if (!Objects.equals(newOTPContain.getPhone(), phone)) {
            model.addFlashAttribute("error", "OTP từ SDT Mới không hợp lệ, vui lòng thực hiện lại");
            otpRepository.delete(oldOTPContain);
            otpRepository.delete(newOTPContain);
            return "redirect:/bank/user";
        }
        User user = currentUser.get();
        user.setPhone(phone);
        userRepository.save(user);
        oldOTPContain.setUsed(true);
        newOTPContain.setUsed(true);
        otpRepository.save(oldOTPContain);
        otpRepository.save(newOTPContain);
        model.addFlashAttribute("success", "Chuyển đổi SĐT thành công.");
        return "redirect:/bank/user";
    }

    @PostMapping("change-avatar")
    public String changeAvatar(
            @RequestParam(defaultValue = "") String avatar,
            RedirectAttributes model
    ) {
        if (avatar == null) {
            model.addFlashAttribute("error", "Ảnh tải lên lỗi!");
            return "redirect:/bank/user";
        }
        User user = currentUser.get();
        user.setAvatar(avatar);
        userRepository.save(user);
        model.addFlashAttribute("success", "Cập nhật thành công");
        return "redirect:/bank/user";
    }
}
