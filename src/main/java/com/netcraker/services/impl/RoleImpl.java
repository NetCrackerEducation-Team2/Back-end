package com.netcraker.services.impl;

import com.netcraker.exceptions.FailedToRegisterException;
import com.netcraker.model.Role;
import com.netcraker.repositories.RoleRepository;
import com.netcraker.services.RoleService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLDataException;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RoleImpl implements RoleService {

    private final @NonNull RoleRepository roleRepository;

    @Override
    public Role createRole(Role role) {

        Role roleFromDB = null;
        try {
            roleFromDB = roleRepository.findByName(role.getName());
        } catch (DataAccessException ignored) {
            // it's alright
        }
        if (roleFromDB != null) {
            throw new FailedToRegisterException("Role is already used");
        }
        return role;
    }

    @Override
    public Role findByRoleId(int roleId) {
        return roleRepository.findById(roleId);
    }

    @Override
    public Role findByRoleName(String name) {
        return roleRepository.findByName(name);
    }

    @Override
    public void updateRole(Role oldRole, Role newRole) {
        roleRepository.updateRole(oldRole, newRole);
    }

    @Override
    public void deleteRole(Role role) throws SQLDataException{

        if(!roleRepository.deleteRole(role)){
            throw new SQLDataException();
        }

    }
}
