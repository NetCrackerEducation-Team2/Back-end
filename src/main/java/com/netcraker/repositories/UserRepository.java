package com.netcraker.repositories;

import com.netcraker.model.User;
import com.netcraker.model.mapper.UserRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Repository
public class UserRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public User findUser(String username, String password) {
        String userSQLQuery = "SELECT * FROM USERS WHERE USERNAME=? AND PASSWORD=?";
        return jdbcTemplate.queryForObject(userSQLQuery, new Object[]{ username, password}, new UserRowMapper());
    }
}