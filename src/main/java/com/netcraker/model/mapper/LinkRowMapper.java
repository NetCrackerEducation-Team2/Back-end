package com.netcraker.model.mapper;

import com.netcraker.model.AuthorizationLinks;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LinkRowMapper implements RowMapper<AuthorizationLinks> {
    @Override
    public AuthorizationLinks mapRow(ResultSet resultSet, int i) throws SQLException {
        AuthorizationLinks authorizationLinks = new AuthorizationLinks();
        authorizationLinks.setLinkId(resultSet.getInt("link_id"));
        authorizationLinks.setUsed(resultSet.getBoolean("used"));
        authorizationLinks.setRegistrationToken(resultSet.getBoolean("is_registration_token"));
        authorizationLinks.setToken(resultSet.getString("token"));
        authorizationLinks.setUserId(resultSet.getInt("user_id"));
        authorizationLinks.setExpiration(resultSet.getTimestamp("expiration"));
        return authorizationLinks;
    }
}
