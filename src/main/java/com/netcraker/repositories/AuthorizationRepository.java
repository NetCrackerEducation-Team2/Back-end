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

    @Value("${spring.queries.find.authorizationLink}")
    private String sqlFindLink;

    @Value("${spring.queries.create.authorizationLinks}")
    private String sqlCreateLink;

    @Value("${spring.queries.update.authorizationLinks}")
    private String sqlUpdateLink;

    public AuthorizationLinks findByActivationCode(String token) {
        return jdbcTemplate.queryForObject(sqlFindLink, new Object[]{ token}, new LinkRowMapper());
    }
    public AuthorizationLinks creteAuthorizationLinks(AuthorizationLinks authorizationLinks) {
        jdbcTemplate.update(sqlCreateLink, new Object[] {authorizationLinks.getToken(),  new Timestamp(System.currentTimeMillis()),
                authorizationLinks.getUserId(), authorizationLinks.isRegistrationToken(),
                authorizationLinks.isUsed()});
        return authorizationLinks;
    }
    public AuthorizationLinks updateAuthorizationLinks(AuthorizationLinks authorizationLinks) {
        jdbcTemplate.update(sqlUpdateLink, authorizationLinks.isUsed(), authorizationLinks.getToken());
        return authorizationLinks;
    }
}
