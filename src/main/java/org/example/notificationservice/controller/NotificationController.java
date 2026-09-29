package org.example.notificationservice.controller;

import org.example.notificationservice.domain.Notification;
import org.example.notificationservice.dto.request.NotificationRequest;
import org.example.notificationservice.dto.response.NotificationResponse;
import org.example.notificationservice.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    public ResponseEntity<NotificationResponse> send(@RequestBody NotificationRequest request){
        Notification notification = notificationService.send(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(notification.getNotificationId())
                .toUri();

        return ResponseEntity.created(location).body(NotificationResponse.from(notification));
    }
    @GetMapping("/{notificationId}")
    public NotificationResponse getById(@PathVariable String notificationId){
        return NotificationResponse.from(notificationService.getById(notificationId));
    }

    @GetMapping(params = "userId")
    public List<NotificationResponse> getByUserId(@RequestParam String userId){
        return notificationService.getByUserId(userId).stream()
                .map(NotificationResponse::from)
                .toList();
    }
}
