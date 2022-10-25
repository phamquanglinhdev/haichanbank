package com.adonis.haichanbank.seed;

import com.adonis.haichanbank.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
public class DataSeeder implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userServices;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent arg0) {
        UserSeeder userSeeder = new UserSeeder(passwordEncoder, userServices);
        userSeeder.run();
    }

}
