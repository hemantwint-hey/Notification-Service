package org.example.notificationservice.decorator;

import java.util.Objects;

public class SignatureNotificationDecorator extends  NotificationDecorator{
    private final String signature;
    public SignatureNotificationDecorator(NotificationContent wrapped,String signature){
        super(wrapped);
         this.signature = Objects.requireNonNull(signature,"signature");
    }
    @Override
    public String getBody(){
        return wrapped.getBody()+"\n\n"+signature;
    }
}
