package com.adonis.haichanbank.config;

import com.adonis.haichanbank.models.User;
import com.adonis.haichanbank.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception)
            throws IOException, ServletException {
        String error = "notbiet";
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        User user = userRepository.findByEmail(email);
        if (user == null) {
            error = "wrong.email";
        } else if (!passwordEncoder.matches(password, user.getPassword())) {
            error = "wrong.password";
        }


        response.sendRedirect("/login?error=" + error);
    }
}