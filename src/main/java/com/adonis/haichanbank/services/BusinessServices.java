package com.adonis.haichanbank.services;

import com.adonis.haichanbank.dto.BusinessDto;
import com.adonis.haichanbank.dto.UserDto;
import com.adonis.haichanbank.models.User;
import com.adonis.haichanbank.repositories.HistoryRepository;
import com.adonis.haichanbank.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BusinessServices {
    @Autowired
    HistoryRepository historyRepository;
    @Autowired
    UserRepository userRepository;

    public BusinessDto getBusiness(String token) {
        User user = userRepository.findByToken(token);
        if (user != null) {
            BusinessDto businessDto = new BusinessDto();
            businessDto.setCallback(user.getCallbackUrl());
            businessDto.setName(user.getName());
            businessDto.setToken(user.getToken());
            return businessDto;
        }
        return null;
    }
}
