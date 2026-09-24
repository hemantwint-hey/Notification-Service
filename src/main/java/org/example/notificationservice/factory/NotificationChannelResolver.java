package org.example.notificationservice.factory;

import org.example.notificationservice.enums.NotificationType;
import org.example.notificationservice.exception.UnsupportedNotificationChannelException;
import org.example.notificationservice.strategy.NotificationChannel;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class NotificationChannelResolver {
        private final Map<NotificationType, NotificationChannel> channelByType;


    public NotificationChannelResolver(List<NotificationChannel> channels){
        Map<NotificationType,NotificationChannel>  map = new EnumMap<>(NotificationType.class);
        for(NotificationChannel channel : channels){
            NotificationChannel previous = map.putIfAbsent(channel.supportedType(), channel);
            if(previous != null){
                throw new IllegalArgumentException("Two channels registered for %s: %s and %s".formatted(
                   channel.supportedType(),
                        previous.getClass().getSimpleName(),
                        channel.getClass().getSimpleName()
                ));
            }
        }
        this.channelByType = Collections.unmodifiableMap(map);
    }
    public NotificationChannel resolve(NotificationType  notificationType){
        NotificationChannel channel = channelByType.get(notificationType);
        if(channel == null){
            throw new UnsupportedNotificationChannelException(notificationType);
        }
        return channel;
    }
}
