package com.adonis.haichanbank.controller;

import com.adonis.haichanbank.dto.NotificationDto;
import com.adonis.haichanbank.dto.UserDto;
import com.adonis.haichanbank.models.Notification;
import com.adonis.haichanbank.models.OTP;
import com.adonis.haichanbank.models.Payment;
import com.adonis.haichanbank.models.User;
import com.adonis.haichanbank.repositories.NotificationRepository;
import com.adonis.haichanbank.repositories.OTPRepository;
import com.adonis.haichanbank.repositories.PaymentRepository;
import com.adonis.haichanbank.services.BusinessServices;
import com.adonis.haichanbank.services.UserServices;
import com.adonis.haichanbank.utils.CurrentUser;
import com.adonis.haichanbank.utils.PhoneNotification;
import com.adonis.haichanbank.utils.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RestController
@CrossOrigin("*")
@RequestMapping("api/v8")
public class ApiController {
    @Autowired
    UserServices userServices;
    @Autowired
    BusinessServices businessServices;
    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    CurrentUser currentUser;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    OTPRepository otpRepository;

    @GetMapping("/card")
    public ResponseEntity<UserDto> cardID(@RequestParam String card) {
        UserDto userDto = new UserDto();
        User user = userServices.getUserByCardId(card);
        if (user == null) {
            user = userServices.getUserByEmail(card);
            if (user == null) {
                user = userServices.getUserByPhone(card);
                if (user == null) {
                    return ResponseEntity.status(404).body(null);
                }
            }
        }
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setCard(user.getCard());
        userDto.setPhone(user.getPhone());
        userDto.setId(user.getId());
        return ResponseEntity.status(200).body(userDto);
    }

    @GetMapping("/auth-1")
    public ResponseEntity<String> checkPassword(@RequestParam String password) throws IOException {
        System.out.println("Đang gửi otp");
        String userPassword = currentUser.get().getPassword();
        if (!passwordEncoder.matches(password, userPassword)) {
            return ResponseEntity.status(404).body(null);
        }
        String userPhone = currentUser.get().getPhone();
        List<OTP> otps = otpRepository.findByPhone(userPhone);
        if (otps != null) {
            for (OTP otp : otps) {
                if (!otp.isUsed()) {
                    otpRepository.delete(otp);
                }
            }
        }
        OTP newOtp = new OTP();
        Random gen = new Random();
        newOtp.setPhone(currentUser.get().getPhone());
        int digest = gen.nextInt((999999 - 111111) + 1) + 111111;
        newOtp.setOtp("H-" + digest);
        otpRepository.save(newOtp);
        PhoneNotification phoneNotification = new PhoneNotification();
        phoneNotification.make(newOtp.getPhone(), "HAICHANBANK OTP:" + newOtp.getOtp());
        return ResponseEntity.status(200).body("Mã OTP đã được gửi tới SĐT ");

    }

    @GetMapping("/new-otp")
    public ResponseEntity<String> newPhoneOTP(@RequestParam String phone) throws IOException {
        User user = userServices.getUserByPhone(phone);
        if (user != null) {
            return ResponseEntity.status(404).body("Số điện thoại đã được sử dụng");
        }
        List<OTP> otps = otpRepository.findByPhone(phone);
        if (otps != null) {
            for (OTP otp : otps) {
                if (!otp.isUsed()) {
                    otpRepository.delete(otp);
                }
            }
        }
        OTP newOtp = new OTP();
        Random gen = new Random();
        newOtp.setPhone(phone);
        int digest = gen.nextInt((999999 - 111111) + 1) + 111111;
        newOtp.setOtp("H-" + digest);
        otpRepository.save(newOtp);
        PhoneNotification phoneNotification = new PhoneNotification();
        phoneNotification.make(newOtp.getPhone(), "HAICHANBANK OTP:" + newOtp.getOtp());
        return ResponseEntity.status(200).body("Mã OTP đã được gửi tới SĐT ");
    }

    @GetMapping("create-payment")
    public String createPayment(
            @RequestParam String token,
            @RequestParam String secret,
            @RequestParam int amount,
            @RequestParam String id,
            @RequestParam String message,
            @RequestParam(defaultValue = "Không có") String detail
    ) {
        Payment payment = new Payment();
        payment.setStatus("pending");
        payment.setPaymentID(id);
        payment.setAmount(amount);
        payment.setMessage(message);
        payment.setToken(token);
        payment.setDetail(detail);
        payment.setTransactionID(RandomString.makeNumberString(12));
        paymentRepository.save(payment);
        return "http:/localhost:8080/bank/payment?token=" + token + "&transaction=" + payment.getTransactionID();
    }

    @Autowired
    NotificationRepository notificationRepository;

    @GetMapping("notifications")
    public ResponseEntity<List<NotificationDto>> getNotifications() {
        User user = currentUser.get();
        List<NotificationDto> notificationDtoList = new ArrayList<>();
        List<Notification> notifications = notificationRepository.findByUserOrderByCreatedDesc(user);
        if (notifications.isEmpty()) {
            return ResponseEntity.status(400).body(null);
        }
        for (Notification notification : notifications) {
            NotificationDto notificationDto = new NotificationDto();
            notificationDto.setTitle(notification.getTitle());
            notificationDto.setMessage(notification.getMessage());
            notificationDto.setReading(notification.getReading());
            notificationDto.setCreated(notification.getCreated());
            notificationDtoList.add(notificationDto);
        }
        return ResponseEntity.status(200).body(notificationDtoList);
    }

    @GetMapping("all-notifications")
    public ResponseEntity<List<NotificationDto>> getAllNotifications() {
        List<NotificationDto> notificationDtoList = new ArrayList<>();
        List<Notification> notifications = notificationRepository.findAll();
        if (notifications.isEmpty()) {
            return ResponseEntity.status(400).body(null);
        }
        for (Notification notification : notifications) {
            NotificationDto notificationDto = new NotificationDto();
            notificationDto.setTitle(notification.getTitle());
            notificationDto.setMessage(notification.getMessage());
            notificationDto.setCreated(notification.getCreated());
            notificationDtoList.add(notificationDto);
        }
        return ResponseEntity.status(200).body(notificationDtoList);
    }

    @GetMapping("notification/read")
    public ResponseEntity<String> read() {
        User user = currentUser.get();
        List<NotificationDto> notificationDtoList = new ArrayList<>();
        List<Notification> notifications = notificationRepository.findByUserOrderByCreatedDesc(user);
        if (notifications.isEmpty()) {
            return ResponseEntity.status(400).body(null);
        }
        for (Notification notification : notifications) {
            if (notification.getReading() == null) {
                notification.setReading("read");
                notificationRepository.save(notification);
            }
        }
        return ResponseEntity.status(200).body(null);
    }

    @GetMapping("avatar")
    public ResponseEntity<String> changeAvatar() {
        return ResponseEntity.status(404).body(null);
    }

}
