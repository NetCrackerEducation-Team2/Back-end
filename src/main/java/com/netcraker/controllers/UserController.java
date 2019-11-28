package com.netcraker.controllers;

import com.netcraker.exceptions.UpdateException;
import com.netcraker.model.Role;
import com.netcraker.model.User;
import com.netcraker.model.vo.ChangePassword;
import com.netcraker.services.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.sql.SQLDataException;
import java.util.List;

@RestController
@CrossOrigin(methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS, RequestMethod.PUT})
@RequestMapping({"api"})
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("profile/{userId}")
    public ResponseEntity<?> getUserProfile(@PathVariable int userId) {
        logger.info("userId = " + userId);
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
        if(result.hasErrors()){
            throw new UpdateException("Password must be valid");
        }

        return ResponseEntity.ok(userService
                .changePassword(userId, changePassword.getOldPassword(), changePassword.getNewPassword()));
    }
}
