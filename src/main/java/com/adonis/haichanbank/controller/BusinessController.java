package com.adonis.haichanbank.controller;

import com.adonis.haichanbank.dto.BusinessDto;
import com.adonis.haichanbank.models.History;
import com.adonis.haichanbank.models.OTP;
import com.adonis.haichanbank.models.Payment;
import com.adonis.haichanbank.models.User;
import com.adonis.haichanbank.repositories.HistoryRepository;
import com.adonis.haichanbank.repositories.OTPRepository;
import com.adonis.haichanbank.repositories.PaymentRepository;
import com.adonis.haichanbank.repositories.UserRepository;
import com.adonis.haichanbank.services.BusinessServices;
import com.adonis.haichanbank.utils.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Objects;

@Controller
@RequestMapping("bank/payment")
public class BusinessController {
    @Autowired
    BusinessServices businessServices;

    @Autowired
    UserRepository userRepository;

    @Autowired
    HistoryRepository historyRepository;

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    OTPRepository otpRepository;
    @Autowired
    CurrentUser currentUser;
    @Autowired
    PasswordEncoder passwordEncoder;


    @GetMapping("")
    public String payment(@RequestParam String token, @RequestParam String transaction, Model model) {
        BusinessDto businessDto = businessServices.getBusiness(token);
        Payment payment = paymentRepository.findByTransactionID(transaction);
        if (payment == null) {
            return "redirect:/bank/index";
        }
        if (Objects.equals(payment.getStatus(), "done")) {
            return "redirect:/bank/index";
        }
        if (businessDto == null) {
            return "redirect:/bank/index";
        }
        model.addAttribute("business", businessDto);
        model.addAttribute("payment", payment);
        model.addAttribute("currentUser", currentUser.get());
        return "business/payment";
    }

    @GetMapping("/save")
    public String makePayment(
            @RequestParam String otp,
            @RequestParam String business,
            @RequestParam String transaction,
            RedirectAttributes model
    ) {
        System.out.println("Đang nhận thông tin");
        BusinessDto businessDto = businessServices.getBusiness(business);
        OTP otp_contain = otpRepository.findByOtp(otp);
        Payment payment = paymentRepository.findByTransactionID(transaction);
        if (otp_contain == null) {
            System.out.println("Sai OTP");
            model.addFlashAttribute("message", "Mã OTP Sai, Vui lòng giao dịch lại!");
            return "redirect:/bank/payment?token=" + business;
        }
        if (businessDto == null) {
            model.addFlashAttribute("message", "Có lỗi xảy ra ! Vui lòng thanh toán lại");
            return "redirect:/bank/payment?token=" + business;
        }
        if (payment == null) {
            model.addFlashAttribute("message", "Có lỗi xảy ra ! Vui lòng thanh toán lại");
            return "redirect:/bank/payment?token=" + business;
        }
        if (Objects.equals(payment.getStatus(), "done")) {
            return "redirect:" + businessDto.getCallback() + "?transaction=" + payment.getTransactionID();
        }
        User fromUser = userRepository.findByEmail(currentUser.get().getEmail());
        User toUser = userRepository.findByToken(businessDto.getToken());
        if (fromUser.getAmount() < payment.getAmount()) {
            model.addFlashAttribute("message", "Số dư không đủ để thực hiện thanh toán");
            return "redirect:/bank/payment?token=" + business;
        }
        fromUser.setAmount(fromUser.getAmount() - payment.getAmount());
        toUser.setAmount(toUser.getAmount() + payment.getAmount());
        History history = new History();
        history.setTo(toUser);
        history.setFrom(fromUser);
        history.setAmount(payment.getAmount());
        history.setMessage("Payment for id " + payment.getPaymentID() + ", transaction id " + payment.getTransactionID());
        historyRepository.save(history);
        userRepository.save(toUser);
        userRepository.save(fromUser);
        otp_contain.setUsed(true);
        payment.setStatus("done");
        otpRepository.save(otp_contain);
        paymentRepository.save(payment);
        return "redirect:" + businessDto.getCallback() + "?transaction=" + payment.getTransactionID();
    }


}
