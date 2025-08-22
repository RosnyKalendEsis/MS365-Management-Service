package com.itm.ms365managementservice.controllers.admin;

import com.itm.ms365managementservice.entities.License;
import com.itm.ms365managementservice.entities.Notifications;
import com.itm.ms365managementservice.entities.User;
import com.itm.ms365managementservice.entities.UserUpdateDTO;
import com.itm.ms365managementservice.services.NotificationService;
import com.itm.ms365managementservice.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final NotificationService notificationService;

    // 🔹 GET /api/users → tous les utilisateurs
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // 🔹 GET /api/users/{id} → un utilisateur par ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 🔹 POST /api/users → créer un utilisateur
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        user.setCreatedDate(ZonedDateTime.now());
        user.setLicenses(new ArrayList<>());
        user.setLicenseExpired(false);
        user.setLicensed(false);
        return ResponseEntity.ok(userService.createUser(user));
    }

    // 🔹 PUT /api/users/{id} → modifier un utilisateur
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody User updatedUser) {
        return ResponseEntity.ok(userService.updateUser(id, updatedUser));
    }

    // 🔹 DELETE /api/users/{id} → supprimer un utilisateur
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // 🔹 GET /api/users/{id}/licenses
    @GetMapping("/{id}/licenses")
    public ResponseEntity<List<License>> getLicensesByUserId(@PathVariable String id) {
        return ResponseEntity.ok(userService.getLicensesByUserId(id));
    }

    @PutMapping("/licenced")
    public ResponseEntity<List<User>> updateLicenced(@RequestBody List<UserUpdateDTO> requests) {
        List<User> updatedUsers = new ArrayList<>();

        for (UserUpdateDTO request : requests) {
            System.out.println("Updating user: " + request.getUserPrincipalName()+":"+request.isLicensed());
            // Récupération du user en base
            User user = userService.getUserByUserPrincipalName(request.getUserPrincipalName());

            if (user != null) {
                System.out.println("licenced user: " + user.getDisplayName()+":"+user.getLicenses());
                // Vérifier si changement de statut licence
                if (user.isLicensed() != request.isLicensed()) {
                    System.out.println("licensed user: " + user.isLicensed());
                    user.setLicensed(request.isLicensed());

                    // Sauvegarder le user mis à jour
                    User savedUser = userService.createUser(user);
                    updatedUsers.add(savedUser);

                    // Créer une notification adaptée
                    Notifications notif = new Notifications();
                    if (request.isLicensed()) {
                        notif.setTitle("Attribution de licence");
                        notif.setBody("La licence a été attribuée à l'utilisateur " + user.getDisplayName() + ".");
                    } else {
                        notif.setTitle("Désactivation de licence");
                        notif.setBody("La licence de l'utilisateur " + user.getDisplayName() + " a été retirée.");
                    }

                    notif.setDateTime(LocalDateTime.now());
                    notif.setMsgRead(false);

                    notificationService.save(notif);
                }
            }
        }

        return ResponseEntity.ok(updatedUsers);
    }

}
