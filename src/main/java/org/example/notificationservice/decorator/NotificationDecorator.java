package org.example.notificationservice.decorator;

import java.util.Objects;

public abstract class NotificationDecorator  implements NotificationContent{
    protected final NotificationContent wrapped;
    protected NotificationDecorator(NotificationContent wrapped){
        this.wrapped = Objects.requireNonNull(wrapped, "wrapped");
    }
    @Override
    public String getTitle(){
        return wrapped.getTitle();
    }
    @Override
    public String getBody(){
        return wrapped.getBody();
    }
}
