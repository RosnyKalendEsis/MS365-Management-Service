package com.itm.ms365managementservice.controllers.admin;

import com.itm.ms365managementservice.entities.Notifications;
import com.itm.ms365managementservice.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    public ResponseEntity<Notifications> create(@RequestBody Notifications notification) {
        return ResponseEntity.ok(notificationService.save(notification));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Notifications> getById(@PathVariable String id) {
        Notifications notification = notificationService.findById(id);
        return notification != null ? ResponseEntity.ok(notification) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<Notifications>> getAll() {
        return ResponseEntity.ok(notificationService.findAll());
    }


    @GetMapping("/read/{read}")
    public ResponseEntity<List<Notifications>> getByReadStatus(@PathVariable boolean read) {
        return ResponseEntity.ok(notificationService.findAllByRead(read));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<Notifications> markAsRead(@PathVariable String id) {
        Notifications updated = notificationService.markAsRead(id);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        notificationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
