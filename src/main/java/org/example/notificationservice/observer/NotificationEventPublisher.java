package org.example.notificationservice.observer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Component
public class NotificationEventPublisher {
    private final CopyOnWriteArrayList<NotificationEventListener> listeners =  new CopyOnWriteArrayList<>();
    public NotificationEventPublisher(List<NotificationEventListener> listeners){
        this.listeners.addAll(listeners);
    }
    public void subscribe(NotificationEventListener listener){
        listeners.addIfAbsent(listener);
    }
    public void unsubscribe(NotificationEventListener listener){
        listeners.remove(listener);
    }
    public void publish(NotificationEvent event){
        for(NotificationEventListener listener:listeners){
            try{
                listener.onEvent(event);
            }
            catch(Exception e){
                log.error("Listener {} failed on {} for notification {}",listener.getClass().getSimpleName(), event.eventType(),event.notificationId(), e);
            }
        }
    }
}
