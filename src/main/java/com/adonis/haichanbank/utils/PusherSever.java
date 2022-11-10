package com.adonis.haichanbank.utils;

import com.adonis.haichanbank.models.User;
import com.pusher.rest.Pusher;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;

public class PusherSever {
    public void sendNotifcation(String chanel_name, String message) {
        Pusher pusher = new Pusher("1472596", "c5318d768de94b3e93c3", "eb006e419305c06b3eb9");
        pusher.setCluster("ap1");
        pusher.trigger(chanel_name, "get-otp", Collections.singletonMap("message", message));
    }
}
