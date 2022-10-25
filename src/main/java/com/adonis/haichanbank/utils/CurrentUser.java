package com.adonis.haichanbank.utils;

import com.adonis.haichanbank.models.User;
import com.adonis.haichanbank.services.UserServicesImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CurrentUser {
    @Autowired
    UserServicesImpl userServices;

    public User get() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userServices.getUserByEmail(authentication.getName());
    }
}
