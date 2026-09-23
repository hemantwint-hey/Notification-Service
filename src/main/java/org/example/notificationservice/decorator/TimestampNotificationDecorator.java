package org.example.notificationservice.decorator;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class TimestampNotificationDecorator extends NotificationDecorator{
    private final String timestamp;
    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm:ss z", Locale.ENGLISH);

    public TimestampNotificationDecorator(NotificationContent wrapped, ZonedDateTime generatedAt) {
        super(wrapped);
        this.timestamp = FORMAT.format(generatedAt);
    }

    @Override
    public String getBody(){
        return wrapped.getBody()+"\n\nTime: "+ timestamp;
    }

}
