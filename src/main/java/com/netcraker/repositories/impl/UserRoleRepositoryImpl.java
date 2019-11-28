package com.netcraker.repositories.impl;

import com.netcraker.exceptions.UpdateException;
import com.netcraker.model.Role;
import com.netcraker.model.User;
import com.netcraker.model.UserRole;
import com.netcraker.model.mapper.UserRoleRowMapper;
import com.netcraker.model.mapper.UserRowMapper;
import com.netcraker.repositories.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@PropertySource("classpath:sqlQueries.properties")
public class UserRoleRepositoryImpl implements UserRoleRepository {

    private static final Logger logger = LoggerFactory.getLogger(UserRoleRepositoryImpl.class);
    private final JdbcTemplate jdbcTemplate;

    @Value("${roleUser.create}")
    private String sqlCreateRoleUser;

    @Value("${roleUser.update}")
    private String sqlUpdateRoleUser;

    @Value("${roleUser.deleteUserId}")
    private String sqlDeleteUserRole;

    @Value("${roleUser.deleteRoleId}")
    private String sqlDeleteRoleUser;

    @Value("${roleUser.findByUserId}")
    private String sqlSelectByUserId;

    public Optional<UserRole> insert(User user, Role role) {
        Object[] params = { user.getUserId(), role.getRoleId()};
        jdbcTemplate.update(sqlCreateRoleUser, params);
        return getById(user.getUserId());
    }
    public Optional<UserRole> getById(int userId) {
        Object[] params = { userId };
        List<UserRole> userRoles =  jdbcTemplate.query(sqlSelectByUserId, params, new UserRoleRowMapper());
        return userRoles.isEmpty() ? Optional.empty() : Optional.of(userRoles.get(0));
    }
    public List<UserRole> getAll(int userId) {
        Object[] params = { userId };
        return jdbcTemplate.query(sqlSelectByUserId, params, new UserRoleRowMapper());
    }

    public Optional<UserRole> update(User user, Role role) {
        Object[] params = {role.getRoleId() , user.getUserId()};
        int changedRowsCount = jdbcTemplate.update(sqlUpdateRoleUser, params);

        if (changedRowsCount == 0){
            throw new UpdateException("Role or user is not found!");
        }
        if (changedRowsCount > 1){
            throw new UpdateException("Multiple update!");
        }
        return getById(user.getUserId());
    }

    public boolean delete(int id)  {
        Object[] params = { id };
        return jdbcTemplate.update(sqlDeleteUserRole, params) == 1;
    }

    @Override
    public Optional<UserRole> insert(UserRole entity) {
        logger.info("trying to add user to db: " + entity);
        Object[] params = {entity.getUserId(), entity.getRoleId()};
        jdbcTemplate.update(sqlCreateRoleUser, params);

        return getById(entity.getUserId());
    }

    @Override
    public Optional<UserRole> update(UserRole entity) {
        return Optional.empty();
    }

}
