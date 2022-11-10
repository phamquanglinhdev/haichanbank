package com.adonis.haichanbank.utils;

import com.slack.api.Slack;
import com.slack.api.webhook.WebhookResponse;

import java.io.IOException;

import static com.slack.api.webhook.WebhookPayloads.*;

public class PhoneNotification {
    public void make(String channel, String phone, String notification) {

        PusherSever pusherSever = new PusherSever();
        pusherSever.sendNotifcation(channel, notification);
    }
}
