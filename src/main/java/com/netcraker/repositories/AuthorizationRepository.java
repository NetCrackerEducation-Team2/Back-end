package com.netcraker.repositories;

import com.netcraker.model.AuthorizationLinks;
import com.netcraker.model.mapper.LinkRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@PropertySource("classpath:sqlQueries.properties")
public class AuthorizationRepository {
    private final JdbcTemplate jdbcTemplate;

    @Value("${spring.queries.create.authorizationLinks}")
    private String sqlCreateLink;

    @Value("${spring.queries.update.authorizationLinks}")
    private String sqlUpdateLink;

    @Value("${spring.find.authorizationLinkByToken}")
    private String sqlFindLinkByToken;

    @Value("${spring.find.authorizationLinkByUserId}")
    private String sqlFindLinkByUserId;

//    @Value("${spring.update.authorizationLinks}")

    public AuthorizationLinks creteAuthorizationLinks(AuthorizationLinks authorizationLinks) {
        jdbcTemplate.update(sqlCreateLink, new Object[] {authorizationLinks.getToken(),  new Timestamp(System.currentTimeMillis()),
                authorizationLinks.getUserId(), authorizationLinks.isRegistrationToken(),
                authorizationLinks.isUsed()});

        return authorizationLinks;
    }
    public AuthorizationLinks findByActivationCode(String token) {
        Object[] param = {token};
        return jdbcTemplate.queryForObject(sqlFindLinkByToken, param, new LinkRowMapper());
    }
    public AuthorizationLinks findByUserId(int user_id) {
        Object[] param = {user_id};
        return jdbcTemplate.queryForObject(sqlFindLinkByUserId, param, new LinkRowMapper());
    }
    public AuthorizationLinks updateAuthorizationLinks(AuthorizationLinks authorizationLinks) {
        jdbcTemplate.update(sqlUpdateLink, authorizationLinks.isUsed(), authorizationLinks.getToken());
        return authorizationLinks;
    }
}
