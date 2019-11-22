package com.netcraker.services.impl;

import com.netcraker.model.AuthorizationLinks;
import com.netcraker.model.User;
import com.netcraker.repositories.AuthorizationRepository;
import com.netcraker.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.Size;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecoveryServiceImpl implements RecoveryService {
    private final EmailSenderService emailSender;
    private final AuthorizationRepository authRepo;
    private final UserService userService;
    private final PasswordGenerator passGenerator;

    @Override
    public boolean sendRecoveryCode(String email) {
        final User fromDb = userService.findByEmail(email);
        if (fromDb == null) return false;
        final AuthorizationLinks recoveryLink = authRepo.createRecoveryLink(fromDb);
        emailSender.sendRecoveryLink(fromDb, recoveryLink);
        return true;
    }

    @Transactional @Override
    public Optional<User> recoverPassword(String recoveryCode) throws NoSuchAlgorithmException {
        AuthorizationLinks linkFromDb;
        try {
            linkFromDb = authRepo.findByActivationCode(recoveryCode);
        } catch (DataAccessException e) {
            System.out.println("RecoveryServiceImpl::recoverPassword. Stack trace: ");
            e.printStackTrace();
            return Optional.empty();
        }
        if (linkFromDb == null || linkFromDb.isUsed())
            return Optional.empty();

        final User userFromDb = userService.findByUserId(linkFromDb.getUserId());
        final String newPassword = passGenerator.generatePassword();
        new Thread(() -> emailSender.sendNewGeneratedPassword(userFromDb, newPassword)).start();

        userFromDb.setPassword(newPassword);
        userService.updateUser(userFromDb, userFromDb);

        linkFromDb.setUsed(true);
        authRepo.updateAuthorizationLinks(linkFromDb);

        return Optional.of(userFromDb);
    }
}
