package com.netcraker.controllers;

import com.netcraker.model.Role;
import com.netcraker.model.User;
import com.netcraker.services.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLDataException;
import java.util.List;

@RestController
@CrossOrigin(methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS, RequestMethod.PUT, RequestMethod.DELETE})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdminModeratorController {
    private final @NonNull UserService userService;

    @PostMapping("admins/create")
    public ResponseEntity<?> createAdminModerator(@RequestBody @Validated User user,
                                                  BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("User must have only valid properties");
        }
        List<Role> roles = user.getRoles();
        userService.createAdminModerator(user, roles);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("admins/update")
    public ResponseEntity<?> updateAdminModerator(@RequestBody User user) {
        List<Role> roles = user.getRoles();
        userService.updateAdminModerator(user, roles);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("admins/{userId}")
    public ResponseEntity<?> getAdminModeratorProfile(@PathVariable int userId) {
        final User userFromDb = userService.findByUserId(userId);
        if (userFromDb == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with such id doesn't exist");
        }
        return ResponseEntity.status(HttpStatus.OK).body(userFromDb);
    }
    @DeleteMapping("admins/delete/{userId}")
    public ResponseEntity<?> deleteAdminModerator(@PathVariable int userId) throws SQLDataException {
        final User userFromDb = userService.findByUserId(userId);
        if (userFromDb == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with such id doesn't exist");
        }
        userService.deleteAdminModerator(userId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
