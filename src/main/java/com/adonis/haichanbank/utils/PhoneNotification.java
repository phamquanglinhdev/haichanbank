package com.adonis.haichanbank.utils;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.Environment;

public class PhoneNotification {
    String account = "ACf6ca46837778d8d824b6bb5eff4d354f";
    String secret = "0a4a2e0d59db6cb42bd8ca2c0f965d63";

    public void physic(String phone, String value) {
        Twilio.init(account, secret);
        Message message = Message.creator(
                        new com.twilio.type.PhoneNumber("+84" + phone),
                        "MG6c11a189bfe26867d26add978703d06c",
                        value)
                .create();
    }

}
