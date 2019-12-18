package com.netcraker.services.impl;

import com.netcraker.exceptions.FailedToRegisterException;
import com.netcraker.model.Role;
import com.netcraker.repositories.RoleRepository;
import com.netcraker.repositories.UserRoleRepository;
import com.netcraker.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RoleIServiceImpl implements RoleService {

    private static final Logger logger = LoggerFactory.getLogger(RoleIServiceImpl.class);
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;

    @Override
    public Role createRole(Role role) {
        Optional<Role> roleFromDB = roleRepository.findByName(role.getName());
        if (roleFromDB.isPresent()) {
            throw new FailedToRegisterException("Role is already created");
        }
        Optional<Role> createdRoleOpt = roleRepository.insert(role);
        if (!createdRoleOpt.isPresent()) {
            throw new FailedToRegisterException("Error in creating role! Creation query failure");
        }
        Role createdRole = createdRoleOpt.get();

        logger.info("created with id: " + createdRole.getRoleId());
        return role;
    }

    @Override
    public Role findByRoleId(int roleId) {
        return roleRepository.getById(roleId).orElse(null);
    }

    @Override
    public Role findByRoleName(String name) {
        return roleRepository.findByName(name).orElse(null);
    }

    @Override
    public void update(Role role) {
        roleRepository.update(role);
    }

    @Override
    public void delete(int id) {
        userRoleRepository.delete(id);
        roleRepository.delete(id);
    }
}
