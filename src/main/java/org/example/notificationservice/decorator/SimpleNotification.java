package org.example.notificationservice.decorator;

import java.util.Objects;

public class SimpleNotification implements NotificationContent {
    private final String title;
    private final String body;

    public SimpleNotification(String title, String body){
        this.title = Objects.requireNonNull(title, "title");
        this.body = Objects.requireNonNull(body, "body");
    }
    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getBody() {
        return body;
    }
}
