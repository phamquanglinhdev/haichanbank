package com.adonis.haichanbank.utils;

import com.slack.api.Slack;
import com.slack.api.webhook.WebhookResponse;

import java.io.IOException;

import static com.slack.api.webhook.WebhookPayloads.*;

public class PhoneNotification {
    public void make(String phone, String notification) throws IOException {
        String webhookUrl = "https://hooks.slack.com/services/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";
        Slack slack = Slack.getInstance();
        WebhookResponse response = slack.send(webhookUrl, payload(
                p -> p
                        .text(notification)
                        .username(phone)
                        .iconUrl("https://haycafe.vn/wp-content/uploads/2022/02/Anh-gai-xinh-Viet-Nam.jpg")

        ));
    }
}
