package com.netcraker.repositories;

import com.netcraker.model.AuthorizationLinks;
import com.netcraker.model.User;
import com.netcraker.model.mapper.UserRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@PropertySource("classpath:sqlQueries.properties")
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    @Value("${spring.queries.create.user}")
    private String sqlCreateUser;

    @Value("${spring.queries.find.user.id}")
    private String sqlSelectUserId;

    @Value("${spring.queries.find.user.email}")
    private String sqlSelectUserEmail;

    private List<User> TEST_USER_LIST = new ArrayList<User>() {
        {
            final User user = new User();
            user.setUserId(1);
            user.setFullName("Alpha Beta");
            user.setEmail("test@test.com");
            user.setPassword("test");
            user.setEnabled(true);

            add(user);
        }
    };

    public User createUser(User user) {
//        jdbcTemplate.update(sqlCreateUser, new Object[] { user.getFull_name(), user.getPassword(), user.getEmail(),
//                                        new Timestamp(System.currentTimeMillis()), true, user.getPhotoPath()});
        TEST_USER_LIST.add(user);
        return user;
    }

    public User findByEmail(String email) {
//        return jdbcTemplate.queryForObject(sqlSelectUserEmail, new Object[]{ username}, new UserRowMapper());
        for (User u : TEST_USER_LIST) {
            if(u.getEmail().equals(email)){
                return u;
            }
        }
        return null;
    }

    public User findByUserId(int userId) {
        for(User u: TEST_USER_LIST){
            if(u.getUserId()==userId){
                return u;
            }
        }
        return null;
//        return jdbcTemplate.queryForObject(sqlSelectUserId, new Object[]{username}, new UserRowMapper());
    }
}