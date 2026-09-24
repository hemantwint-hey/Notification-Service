package org.example.notificationservice.observer;

import org.example.notificationservice.enums.NotificationType;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.atomic.LongAdder;

@Component
public class NotificationMetricsListener implements NotificationEventListener {
    private final Map<NotificationEventType, LongAdder> counters = new EnumMap<>(NotificationEventType.class);

    public NotificationMetricsListener(){
        for(NotificationEventType type : NotificationEventType.values()){
            counters.put(type, new LongAdder());
        }
    }

    @Override
    public void onEvent(NotificationEvent event) {
        counters.get(event.eventType()).increment();
    }
    public long count(NotificationEventType type){
        return counters.get(type).sum();
    }
}
