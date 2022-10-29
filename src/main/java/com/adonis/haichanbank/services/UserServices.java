package com.adonis.haichanbank.services;

import com.adonis.haichanbank.models.User;

import java.util.List;
import java.util.Optional;

public interface UserServices {
    List<User> getAllUsers();

    Optional<User> getUserById(Long id);

    User getUserByEmail(String email);

    void deleteUserById(Long id);

    void saveUser(User user);

    User getUserByCardId(String card);
    User getUserByPhone(String phone);
}
