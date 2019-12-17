package com.netcraker.repositories;

import com.netcraker.model.AuthorizationLinks;
import com.netcraker.model.User;

import java.util.Optional;

public interface AuthorizationRepository {
    AuthorizationLinks creteAuthorizationLinks(User user);
    AuthorizationLinks createRecoveryLink(User user);
    Optional<AuthorizationLinks> findByActivationCode(String token);
    AuthorizationLinks findByUserId(int user_id);
    AuthorizationLinks updateAuthorizationLinks(AuthorizationLinks authorizationLinks);
}
