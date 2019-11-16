package com.netcraker.repositories;

import com.netcraker.exceptions.UpdateException;
import com.netcraker.model.Role;
import com.netcraker.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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

    public void createUserRole(User user, Role role) {
        Object[] params = { role.getRoleId(), user.getUserId() };
        jdbcTemplate.update(sqlCreateRoleUser, params);
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

    public boolean deleteUserRole(User user)  {
        Object[] params = {user.getUserId()};
        int changedRowsCount = jdbcTemplate.update(sqlDeleteRoleUser, params);
        return changedRowsCount == 1;
    }

}
