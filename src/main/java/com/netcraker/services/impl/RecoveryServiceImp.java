package com.netcraker.services.impl;

import com.netcraker.model.AuthorizationLinks;
import com.netcraker.model.User;
import com.netcraker.repositories.AuthorizationRepository;
import com.netcraker.services.AuthEmailSenderService;
import com.netcraker.services.PasswordGenerator;
import com.netcraker.services.RecoveryService;
import com.netcraker.services.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class RecoveryServiceImp implements RecoveryService {
    private final AuthEmailSenderService emailSender;
    private final AuthorizationRepository authRepo;
    private final UserService userService;
    private final PasswordEncoder encoder;
    private final PasswordGenerator passGenerator;
    private static final Logger logger = LoggerFactory.getLogger(RecoveryServiceImp.class);
    @Override
    public boolean sendRecoveryCode(String email) {
        final User fromDb = userService.findByEmail(email);
        if (fromDb == null) return false;
        final AuthorizationLinks recoveryLink = authRepo.createRecoveryLink(fromDb);
        emailSender.sendRecoveryLink(fromDb, recoveryLink);
        return true;
    }

    @Transactional
    @Override
    public Optional<User> recoverPassword(String recoveryCode) {
        Optional<AuthorizationLinks> linkFromDb = authRepo.findByActivationCode(recoveryCode);
        if(!linkFromDb.isPresent() || linkFromDb.get().isUsed()){
            logger.error("RecoveryServiceImpl::recoverPassword. Stack trace: ");
            return Optional.empty();
        }

        final User userFromDb = userService.findByUserId(linkFromDb.get().getUserId());

        final String newPassword = passGenerator.generatePassword();

        emailSender.sendNewGeneratedPassword(userFromDb, newPassword);

        userFromDb.setPassword(encoder.encode(newPassword));
        userService.updateUser(userFromDb, userFromDb);

        linkFromDb.get().setUsed(true);
        authRepo.updateAuthorizationLinks(linkFromDb.get());

        return Optional.of(userFromDb);
    }
}
