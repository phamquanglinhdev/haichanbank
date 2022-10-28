package com.adonis.haichanbank.utils;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class PhoneNotification {
    public final String ACCOUNT_SID = "ACf6ca46837778d8d824b6bb5eff4d354f";
    public final String AUTH_TOKEN = "d59dea3c72c2007b08029309958a66ca";

    public void make(String phone, String notification) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(
                        new PhoneNumber("+84" + phone),
                        new PhoneNumber("+17745458566"),
                        notification)
                .create();
//
//        System.out.println(message.getSid());
    }
}
