package org.example.notificationservice.exception;

import java.util.List;

public class InvalidNotificationRequestException extends RuntimeException{
    private final List<String> violations;
    public InvalidNotificationRequestException(List<String> violations){
        super("Invalid Notification request"+String.join(";",violations));
        this.violations = List.copyOf(violations);
    }
    public List<String> getViolations(){
        return violations;
    }
}
