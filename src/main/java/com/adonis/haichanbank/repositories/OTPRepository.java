package com.adonis.haichanbank.repositories;

import com.adonis.haichanbank.models.OTP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OTPRepository extends JpaRepository<OTP, Long> {
    List<OTP> findByPhone(String phone);

    OTP findByOtp(String otp);
}
