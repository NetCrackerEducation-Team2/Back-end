package com.netcraker.controllers;

import com.netcraker.exceptions.UpdateException;
import com.netcraker.model.User;
import com.netcraker.model.vo.ChangePassword;
import com.netcraker.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS, RequestMethod.PUT})
@RequestMapping({"api"})
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

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

    @PutMapping("profile/change-password/{userId}")
    public ResponseEntity<?> changePassword(@PathVariable int userId,
                                            @RequestBody @Validated ChangePassword changePassword,
                                            BindingResult result) {
        if (result.hasErrors()) {
            throw new UpdateException("Password must be valid");
        }

        return ResponseEntity.ok(userService
                .changePassword(userId, changePassword.getOldPassword(), changePassword.getNewPassword()));
    }

    @GetMapping("profile/search")
    public List<User> searchUser(@RequestParam String searchExpression, HttpServletRequest request) {
        return userService.searchUser(searchExpression, getCurrentUser(request));
    }

    private Optional<User> getCurrentUser(HttpServletRequest request) {
        String currentUserEmail = (String) request.getAttribute("currentUserEmail");
        return Optional.ofNullable(userService.findByEmail(currentUserEmail));
    }
}
