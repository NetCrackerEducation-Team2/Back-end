package com.netcraker.repositories;

import com.netcraker.exceptions.UpdateException;
import com.netcraker.model.Role;
import com.netcraker.model.User;
import com.netcraker.model.UserRole;
import com.netcraker.model.mapper.UserRoleRowMapper;
import com.netcraker.model.mapper.UserRowMapper;
import lombok.RequiredArgsConstructor;
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
public class UserRoleRepository {

    private final JdbcTemplate jdbcTemplate;

    @Value("${roleUser.create}")
    private String sqlCreateRoleUser;

    @Value("${roleUser.update}")
    private String sqlUpdateRoleUser;

    @Value("${roleUser.delete}")
    private String sqlDeleteRoleUser;

    @Value("${roleUser.findByUserRoleId}")
    private String sqlSelectUserRoleId;

    public Optional<UserRole> createUserRole(User user, Role role) {
        Object[] params = { user.getUserId(), role.getRoleId()};
        jdbcTemplate.update(sqlCreateRoleUser, params);
        return getById(user.getUserId());
    }
    public Optional<UserRole> getById(int userId) {
        Object[] params = { userId };
        List<UserRole> userRoles =  jdbcTemplate.query(sqlSelectUserRoleId, params, new UserRoleRowMapper());
        return userRoles.isEmpty() ? Optional.empty() : Optional.of(userRoles.get(0));
    }

    public void updateUserRole(Role oldRole, User newUser) {
        Object[] params = { newUser.getUserId(), oldRole.getRoleId()};
        int changedRowsCount = jdbcTemplate.update(sqlUpdateRoleUser, params);

        if (changedRowsCount == 0){
            throw new UpdateException("Role or user is not found!");
        }
        if (changedRowsCount > 1){
            throw new UpdateException("Multiple update!");
        }

    }

    public boolean delete(User user)  {
        Object[] params = {user.getUserId()};
        int changedRowsCount = jdbcTemplate.update(sqlDeleteRoleUser, params);
        return changedRowsCount == 1;
    }

}
