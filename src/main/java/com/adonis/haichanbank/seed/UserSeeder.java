package com.adonis.haichanbank.seed;

import com.adonis.haichanbank.models.User;
import com.adonis.haichanbank.repositories.UserRepository;
import com.adonis.haichanbank.services.UserServicesImpl;
import com.adonis.haichanbank.utils.RandomString;
import jdk.jfr.Enabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;


import javax.websocket.Encoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.random.RandomGenerator;

@Enabled
public class UserSeeder {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserSeeder(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Bean
    public void run() {
        if (userRepository.findByEmail("phamquanglinhdev@gmail.com") == null) {
            Random gen = new Random();
            int LastDigest = gen.nextInt((9999 - 1111) + 1) + 1111;
            int midDigest = gen.nextInt((9999 - 1111) + 1) + 1111;
            User user = new User();
            user.setName("Phạm Quang Linh");
            user.setEmail("phamquanglinhdev@gmail.com");
            user.setPassword(passwordEncoder.encode("Linhz123@"));
            user.setAmount(10000);
            user.setPhone("0904800240");
            user.setRole("admin");
            user.setRemember("AIDJASPOmoquwdq90sd");
            user.setCard("9704 1801 " + midDigest + " " + LastDigest);
            user.setAvatar("https://cf.shopee.vn/file/aded53959846e8c6e6a9349f8afaf0fb");
            userRepository.save(user);
        }
        if (userRepository.findByEmail("user1@gmail.com") == null) {
            Random gen = new Random();
            int LastDigest = gen.nextInt((9999 - 1111) + 1) + 1111;
            int midDigest = gen.nextInt((9999 - 1111) + 1) + 1111;
            User user = new User();
            user.setName("Nguyễn Văn A");
            user.setEmail("user1@gmail.com");
            user.setPassword(passwordEncoder.encode("Linhz123@"));
            user.setAmount(10000);
            user.setPhone("0909009009");
            user.setRole("user");
            user.setRemember("AIDJASPOmoquwdq90sd");
            user.setCard("9704 1801 " + midDigest + " " + LastDigest);
            user.setAvatar("https://s199.imacdn.com/ta/2018/02/05/6f6b6137f1747a4d_8af7064721ba13bb_10643415178258735143215.jpg");
            userRepository.save(user);
        }

        if (userRepository.findByEmail("business1@gmail.com") == null) {
            Random gen = new Random();
            int LastDigest = gen.nextInt((9999 - 1111) + 1) + 1111;
            int midDigest = gen.nextInt((9999 - 1111) + 1) + 1111;
            User user = new User();
            user.setName("Cửa hàng ABC");
            user.setEmail("business1@gmail.com");
            user.setPassword(passwordEncoder.encode("Linhz123@"));
            user.setAmount(10000);
            user.setPhone("0897778337");
            user.setRole("bussiness");
            user.setRemember("AIDJASPOmoquwdq90sd");
            user.setCard("9704 1801 " + midDigest + " " + LastDigest);

            user.setToken(RandomString.make(50));
            user.setCallbackUrl("https://laptrinhluon.com/callback");
            user.setAvatar("https://s199.imacdn.com/ta/2018/02/05/6f6b6137f1747a4d_8af7064721ba13bb_10643415178258735143215.jpg");
            userRepository.save(user);
        }
    }
}
