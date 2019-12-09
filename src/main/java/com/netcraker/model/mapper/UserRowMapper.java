package com.netcraker.model.mapper;
import com.netcraker.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
@Builder
@AllArgsConstructor
public class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet resultSet, int i) throws SQLException {
        User user = new User();
        user.setUserId(resultSet.getInt("user_id"));
        user.setFullName(resultSet.getString("full_name"));
        user.setPassword(resultSet.getString("password"));
        user.setEmail(resultSet.getString("email"));
        user.setEnabled(resultSet.getBoolean("enabled"));
        user.setCreatedAt(resultSet.getTimestamp("creation_time"));
        user.setPhotoPath(resultSet.getString("photo_path"));
        return  user;
    }
}
