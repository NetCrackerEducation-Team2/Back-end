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
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@PropertySource("classpath:sqlQueries.properties")
public class AuthorizationRepository {

    private final JdbcTemplate jdbcTemplate;

    @Value("${spring.queries.find.authorizationLink}")
    private String sqlFindLink;

    @Value("${spring.queries.create.authorizationLinks}")
    private String sqlCreateLink;

    @Value("${spring.queries.update.authorizationLinks}")
    private String sqlUpdateLink;


    public AuthorizationLinks findByActivationCode(String token) {
        Object[] param = {token};
        return jdbcTemplate.queryForObject(sqlFindLink, param, new LinkRowMapper());
    }
    public AuthorizationLinks creteAuthorizationLinks(AuthorizationLinks authorizationLinks) {
        Object[] param = {authorizationLinks.getToken(),  new Timestamp(System.currentTimeMillis()),
                        authorizationLinks.getUserId(), authorizationLinks.isRegistrationToken(),
                        authorizationLinks.isUsed()};
        jdbcTemplate.update(sqlCreateLink, param);

        return authorizationLinks;
    }
    public AuthorizationLinks updateAuthorizationLinks(AuthorizationLinks authorizationLinks) {
        jdbcTemplate.update(sqlUpdateLink, authorizationLinks.isUsed(), authorizationLinks.getToken());
        return authorizationLinks;
    }
}
