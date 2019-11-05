package com.netcraker.repositories;
import com.netcraker.model.User;
import com.netcraker.model.mapper.UserRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.sql.*;

@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public User getUser(String firstName, String password) {
        String userSQLQuery = "SELECT * FROM users WHERE first_name=? AND password=?";
        return jdbcTemplate.queryForObject(userSQLQuery, new Object[]{ firstName, password}, new UserRowMapper());
    }

//    public List<Role> getRoles(String username) {
//        List<Map<String, Object>> results = jdbcTemplate
//                .queryForList("select user_role from user_role where user_name = ?", new Object[] { username });
//        List<Role> roles = results.stream().map(m -> {
//            Role role = new Role();
//            role.setName(String.valueOf(m.get("user_role")));
//            return role;
//        }).collect(Collectors.toList());
//        return roles;
//    }

    public User createUser(User user) {
        user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        jdbcTemplate.update("INSERT INTO users (first_name, last_name, password, email, creation_time, enabled, photo_path)" +
                                "VALUES(?, ?, ?, ?, ?, '1', ?)",
                new Object[] { user.getFirstName(), user.getLastName(), user.getPassword(), user.getEmail(),
                        user.getCreatedAt(), user.isEnabled(), user.getPhotoPath()});
//        user.getRoles().forEach(r -> jdbcTemplate.update(new PreparedStatementCreator() {
//            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
//                PreparedStatement ps = connection.prepareStatement(
//                        "INSERT INTO user_role(first_name, user_role) values(?, ?)",
//                        new String[] { "user_name", "user_role" });
//                ps.setString(1, user.getFirstName());
//                ps.setString(2, r);
//                return ps;
//            }
//        }));
        return user;
    }
}