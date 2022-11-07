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
                p -> p.text(phone + ":" + notification)
        ));
    }
}
