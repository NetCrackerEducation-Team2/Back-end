package com.netcraker.controllers;

import com.netcraker.model.Role;
import com.netcraker.model.User;
import com.netcraker.repositories.UserRepository;
import com.netcraker.services.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin(methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {
    private final @NonNull UserService userService;

    @GetMapping("profile/{userId}")
    public ResponseEntity<?> getUserProfile(@PathVariable int userId) {
        System.out.println("userId = " + userId);
        final User userFromDb = userService.findByUserId(userId);
        if (userFromDb == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with such id doesn't exist");
        }
        return ResponseEntity.status(HttpStatus.OK).body(userFromDb);
    }

    @PutMapping("profile/update")
    public ResponseEntity<?> updateUserProfile(@RequestBody List<User> users) {
        userService.updateUser(users.get(0), users.get(1));
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("admins/create")
    public ResponseEntity<?> createAdminModerator(@RequestBody @Validated User user,
                                      @RequestBody Role role,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("User must have only valid properties");
        }
        userService.createAdminModerator(user, role);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
