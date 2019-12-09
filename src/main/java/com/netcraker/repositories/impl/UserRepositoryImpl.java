package com.netcraker.repositories.impl;

import com.netcraker.exceptions.FindException;
import com.netcraker.exceptions.UpdateException;
import com.netcraker.model.Role;
import com.netcraker.model.User;
import com.netcraker.model.mapper.UserRowMapper;
import com.netcraker.repositories.UserRepository;
import com.netcraker.repositories.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@PropertySource("classpath:sqlQueries.properties")
public class UserRepositoryImpl implements UserRepository {

    private static final Logger logger = LoggerFactory.getLogger(UserRepositoryImpl.class);
    private final UserRoleRepository userRoleRepository;
    private final JdbcTemplate jdbcTemplate;

    @Value("${user.findByEmail}")
    private String sqlFindByEmail;

    @Value("${user.activate}")
    private String sqlActivate;

    @Value("${user.getById}")
    private String sqlGetById;

    @Value("${user.insert}")
    private String sqlInsert;

    @Value("${user.update}")
    private String sqlUpdate;

    @Value("${user.delete}")
    private String sqlDelete;

    @Value("${user.findByEmailOrFullNameFilterByRole}")
    private String sqlFindByEmailOrFullNameFilterByRole;

    @Value("${user.findByEmailOrFullNameFilterByRoleWithout}")
    private String sqlFindByEmailOrFullNameFilterByRoleWithout;

    @Value("${user.findByEmailOrFullNameFilterByRoleCount}")
    private String sqlFindByEmailOrFullNameFilterByRoleCount;

    @Value("${user.findByEmailOrFullNameFilterByRoleWithoutCount}")
    private String sqlFindByEmailOrFullNameFilterByRoleWithoutCount;

    @Value("${user.deleteByEmail}")
    private String sqlDeleteByEmail;
    @Value("${user.getListId}")
    private String sqlListId;

    @Value("${users_localChat.getById}")
    private String sqlListUsers;

    @Value("${user.searchByFullNameContains}")
    private String sqlSearchByNameContains;

    @Override
    public Optional<User> findByEmail(String email) {
        Object[] params = {email};
        List<User> users = jdbcTemplate.query(sqlFindByEmail, params, new UserRowMapper());
        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }

    @Override
    public Optional<User> activateUser(String email) {
        Optional<User> entity = findByEmail(email);
        if (!entity.isPresent())
            throw new FindException("User is not activated! User is not found.");

        User user = entity.get();
        Object[] params = {true, user.getUserId(), user.getEmail(), user.getPassword()};
        int changedRowsCount = jdbcTemplate.update(sqlActivate, params);

        if (changedRowsCount == 0)
            throw new UpdateException("User is not activated! User is not found.");
        if (changedRowsCount > 1)
            throw new UpdateException("User is not activated! Multiple update! Only one user can be changed.");

        return entity;
    }

    @Override
    public int getFindByEmailOrFullNameFilterByRoleCount(String searchExpression, Role roleFiltering) {
        return jdbcTemplate.queryForObject(sqlFindByEmailOrFullNameFilterByRoleCount, int.class, roleFiltering.getRoleId(), searchExpression, searchExpression);

    }

    @Override
    public int getFindByEmailOrFullNameFilterByRoleWithoutCount(String searchExpression, Role roleFilteringWithout) {
        return jdbcTemplate.queryForObject(sqlFindByEmailOrFullNameFilterByRoleWithoutCount, int.class, roleFilteringWithout.getRoleId(), searchExpression, searchExpression);
    }

    @Override
    public List<User> findByEmailOrFullNameFilterByRole(String searchExpression, Role roleFiltering, int offset, int pageSize) {
        return jdbcTemplate.query(sqlFindByEmailOrFullNameFilterByRole,
                new UserRowMapper(),
                roleFiltering.getRoleId(),
                searchExpression,
                searchExpression,
                pageSize,
                offset
        );
    }

    @Override
    public List<User> findByEmailOrFullNameFilterByRoleWithout(String searchExpression, Role roleWithout, int offset, int pageSize) {
        return jdbcTemplate.query(sqlFindByEmailOrFullNameFilterByRoleWithout,
                new UserRowMapper(),
                roleWithout.getRoleId(),
                searchExpression,
                searchExpression,
                pageSize,
                offset
        );
    }

    @Override
    public Optional<User> getById(int id) {
        Object[] params = {id};
        List<User> users = jdbcTemplate.query(sqlGetById, params, new UserRowMapper());
        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }
    @Transactional
    @Override
    public Optional<User> insert(User entity) {
        logger.info("trying to add user to db: " + entity);
        Object[] params = {entity.getFullName(), entity.getPassword(), entity.getEmail(),
                new Timestamp(System.currentTimeMillis()), entity.getEnabled(), entity.getPhotoPath()};
        jdbcTemplate.update(sqlInsert, params);
        for (Role role : entity.getRoles()) {
            userRoleRepository.insert(entity, role);
        }
        return findByEmail(entity.getEmail());
    }

    @Override
    public Optional<User> update(User entity) {
        Object[] params = {entity.getPassword(),
                entity.getEmail(),
                entity.getEnabled(),
                entity.getPhotoPath(),
                entity.getFullName(),
                entity.getUserId()};

        int changedRowsCount = jdbcTemplate.update(sqlUpdate, params);

        if (changedRowsCount == 0)
            throw new UpdateException("User is not found!");
        if (changedRowsCount > 1)
            throw new UpdateException("Multiple update! Only one user can be changed!");
        Optional<User> user = getById(entity.getUserId());
        return getById(entity.getUserId());
    }

    @Override
    public boolean deleteByEmail(String email) {
        Object[] params = {email};
        return jdbcTemplate.update(sqlDeleteByEmail, params) == 1;
    }

    @Override
    public boolean delete(int id) {
        Object[] params = {id};
        return jdbcTemplate.update(sqlDelete, params) == 1;
    }

    @Override
    // public List<User> searchByNameContains(String authorFullNameContains) {
    //     return jdbcTemplate.query(sqlSearchByNameContains, new UserRowMapper(), "%" + authorFullNameContains.trim() + "%");

    public List<Integer> getListId() {
        return jdbcTemplate.queryForList(sqlListId, Integer.class);
    }
}
