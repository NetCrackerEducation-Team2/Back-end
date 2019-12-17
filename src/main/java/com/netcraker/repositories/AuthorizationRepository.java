package com.netcraker.repositories;

import com.netcraker.model.AuthorizationLinks;
import com.netcraker.model.User;

public interface AuthorizationRepository {
    AuthorizationLinks creteAuthorizationLinks(User user);
    AuthorizationLinks createRecoveryLink(User user);
    AuthorizationLinks findByActivationCode(String token);
    AuthorizationLinks findByUserId(int user_id);
    AuthorizationLinks updateAuthorizationLinks(AuthorizationLinks authorizationLinks);
}
