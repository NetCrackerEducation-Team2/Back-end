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
import java.util.List;

@RestController
@CrossOrigin(methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @PostMapping("/role/create")
    public ResponseEntity<?> createRole(@RequestBody @Validated Role role, BindingResult bindingResult){

        if (bindingResult.hasErrors()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Role must have only valid properties");
        }
        roleService.createRole(role);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("role/{roleId}")
    public ResponseEntity<?> getRole(@PathVariable int roleId) {
        final Role roleFromDb = roleService.findByRoleId(roleId);
        if (roleFromDb == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Role with such id doesn't exist");
        }
        return ResponseEntity.status(HttpStatus.OK).body(roleFromDb);
    }

    @PutMapping("role/update")
    public ResponseEntity<?> updateRole(@RequestBody List<Role> role) {
        roleService.update(role.get(0));
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("role/delete/{roleId}")
    public ResponseEntity<?> deleteRole(@PathVariable int roleId) throws SQLDataException {
        final Role roleFromDb = roleService.findByRoleId(roleId);
        if (roleFromDb == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Role with such id doesn't exist");
        }
        roleService.delete(roleId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
