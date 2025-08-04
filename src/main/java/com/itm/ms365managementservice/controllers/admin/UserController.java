package com.itm.ms365managementservice.controllers.admin;

import com.itm.ms365managementservice.entities.License;
import com.itm.ms365managementservice.entities.User;
import com.itm.ms365managementservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

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
}
