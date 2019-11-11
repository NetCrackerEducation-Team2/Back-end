package com.netcraker.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netcraker.model.Role;
import com.netcraker.services.RoleService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLDataException;

@RestController
@CrossOrigin(methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RoleController {
    private final @NonNull RoleService roleService;

    @PostMapping("/role/create")
    public ResponseEntity<?> createUserRole(@RequestBody @Validated Role role, BindingResult bindingResult){

        if (bindingResult.hasErrors()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Role must have only valid properties");
        }
        roleService.createRole(role);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("role/{roleId}")
    public ResponseEntity<?> getUserRole(@PathVariable int roleId) {
        final Role roleFromDb = roleService.findByRoleId(roleId);
        if (roleFromDb == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Role with such id doesn't exist");
        }
        return ResponseEntity.status(HttpStatus.OK).body(roleFromDb);
    }

    @PutMapping("role/update")
    public ResponseEntity<?> updateUserRole(@RequestBody Role oldRole, @RequestBody Role newRole) {
        roleService.updateRole(oldRole, newRole);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("role/delete")
    public ResponseEntity<?> deleteUserRole(@RequestBody int roleId) throws SQLDataException {
        final Role roleFromDb = roleService.findByRoleId(roleId);
        if (rolFromDb == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Role with such id doesn't exist");
        }
        roleService.deleteRole(rolFromDb);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
