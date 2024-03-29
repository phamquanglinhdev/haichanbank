package com.adonis.haichanbank.controller;

import com.adonis.haichanbank.models.History;
import com.adonis.haichanbank.models.Notification;
import com.adonis.haichanbank.models.OTP;
import com.adonis.haichanbank.models.User;
import com.adonis.haichanbank.repositories.HistoryRepository;
import com.adonis.haichanbank.repositories.NotificationRepository;
import com.adonis.haichanbank.repositories.OTPRepository;
import com.adonis.haichanbank.repositories.UserRepository;
import com.adonis.haichanbank.utils.PhoneNotification;
import com.adonis.haichanbank.utils.PusherSever;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.naming.Binding;
import javax.persistence.EntityResult;
import java.security.Principal;
import java.text.NumberFormat;
import java.util.Objects;

@Controller
@RequestMapping("bank")
public class MoneyController {
    @Autowired
    HistoryRepository historyRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    OTPRepository otpRepository;

    @Autowired
    NotificationRepository notificationRepository;

    @PostMapping("transfer")
    public String TransferMoney(@ModelAttribute History history, BindingResult bindingResult, @RequestParam(name = "otp") String otp_code, RedirectAttributes model, Principal principal) throws BindException {
        if (bindingResult.hasErrors()) {
            model.addFlashAttribute("error", "Đã có lỗi xảy ra , vui lòng kiểm tra lại (VA)");
            return "redirect:/bank";
        }
        if (history.getTo() == null) {
            model.addFlashAttribute("error", "Không có người nhận");
            return "redirect:/bank";
        }
        User fromUser = userRepository.findByEmail(principal.getName());
        if (fromUser == history.getTo()) {
            model.addFlashAttribute("error", "Không thể gửi cho chính mình");
            return "redirect:/bank";
        }
        OTP otp_contain = otpRepository.findByOtp(otp_code);
        if (otp_contain == null) {
            model.addFlashAttribute("error", "Không tồn tại OTP");
            return "redirect:/bank";
        } else if (!Objects.equals(otp_contain.getPhone(), fromUser.getPhone())) {
            model.addFlashAttribute("error", "OTP lỗi , vui lòng gửi lại");
            return "redirect:/bank";
        }
        if (otp_contain.isUsed()) {
            model.addFlashAttribute("error", "OTP lỗi , vui lòng gửi lại");
            return "redirect:/bank";
        }
        if (Objects.equals(history.getMessage(), "")) {
            history.setMessage(fromUser.getName() + " chuyển tiền.");
        }
        otp_contain.setUsed(true);
        otpRepository.save(otp_contain);
        history.setFrom(fromUser);
        User toUser = history.getTo();
        int amount = history.getAmount();
        if (amount > fromUser.getAmount()) {
            model.addFlashAttribute("error", "Số tiền không đủ để chuyển khoản");
            return "redirect:/bank";
        }
        try {
            fromUser.setAmount(fromUser.getAmount() - amount);
        } catch (Exception exception) {
            model.addFlashAttribute("error", "Vui lòng nhập số tiền !");
            return "redirect:/bank";
        }
        toUser.setAmount(toUser.getAmount() + amount);
        userRepository.save(fromUser);
        userRepository.save(toUser);
        historyRepository.save(history);
        Notification formNotification = new Notification();
        formNotification.setUser(fromUser);
        formNotification.setTitle("Biến động số dư");
        formNotification.setMessage("TK:" + fromUser.getCard() + " -" + amount + "đ. ND:" + history.getMessage());
        notificationRepository.save(formNotification);
        PhoneNotification phoneFromNotification = new PhoneNotification();
        phoneFromNotification.physic(fromUser.getPhone(), formNotification.getMessage());
        Notification toNotification = new Notification();
        toNotification.setUser(toUser);
        toNotification.setTitle("Biến động số dư");
        toNotification.setMessage("TK:" + toUser.getCard() + " +" + amount + "đ. ND:" + history.getMessage());
        notificationRepository.save(toNotification);
        PhoneNotification phoneToNotification = new PhoneNotification();
        phoneToNotification.physic(toUser.getPhone(), toNotification.getMessage());
        model.addFlashAttribute("success", "Thành công !");
        return "redirect:/bank";
    }
}
