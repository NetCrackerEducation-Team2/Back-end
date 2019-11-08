package com.netcraker.repositories;

import com.netcraker.model.AuthorizationLinks;
import com.netcraker.model.User;
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

//    List<AuthorizationLinks> TEST_AUTHLINKS = new ArrayList<AuthorizationLinks>(){{
//
//    }};

    public AuthorizationLinks findByActivationCode(String token) {
        return jdbcTemplate.queryForObject(sqlFindLink, new Object[]{ token}, new LinkRowMapper());
//        for(AuthorizationLinks link: TEST_AUTHLINKS){
//            if(token.equals(link.getToken())){
//                return link;
//            }
//        }
//        return null;
    }
    public AuthorizationLinks creteAuthorizationLinks(AuthorizationLinks authorizationLinks) {
        jdbcTemplate.update(sqlCreateLink, new Object[] {authorizationLinks.getToken(),  new Timestamp(System.currentTimeMillis()),
                authorizationLinks.getUserId(), authorizationLinks.isRegistrationToken(),
                authorizationLinks.isUsed()});
//
//        TEST_AUTHLINKS.add(authorizationLinks);

        return authorizationLinks;
    }
    public AuthorizationLinks updateAuthorizationLinks(AuthorizationLinks authorizationLinks) {
        jdbcTemplate.update(sqlUpdateLink, authorizationLinks.isUsed(), authorizationLinks.getToken());
        return authorizationLinks;
    }
}
