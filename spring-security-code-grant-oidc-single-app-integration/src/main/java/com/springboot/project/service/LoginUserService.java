package com.springboot.project.service;

import com.springboot.project.entity.LoginUserEntity;
import com.springboot.project.exception.BadCredentialException;
import com.springboot.project.exception.ResourceNotFoundException;
import com.springboot.project.generated.model.LoginUserResponseModel;
import com.springboot.project.mapper.LoginUserMapper;
import com.springboot.project.model.LoginUserModel;
import com.springboot.project.repository.LoginUserRepository;
import java.text.MessageFormat;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class LoginUserService {

    private static final String LOGIN_USER_NOT_FOUND = "Login user with email {0} not found";
    private static final String BAD_CREDENTIAL_EXCEPTION = "Bad credential";

    private final LoginUserRepository loginUserRepository;

    @Autowired
    public LoginUserService(LoginUserRepository loginUserRepository) {
        this.loginUserRepository = loginUserRepository;
    }

    public void upsertLoginUser(LoginUserModel loginUserModelRequest) {
        Optional<LoginUserEntity> loginUserEntityOptional =
                this.loginUserRepository.findByEmail(loginUserModelRequest.getEmail());
        if (loginUserEntityOptional.isPresent()) {
            LoginUserEntity loginUserEntity = loginUserEntityOptional.get();
            LoginUserMapper.MAPPER.updateLoginUserEntity(loginUserEntity, loginUserModelRequest);
            this.loginUserRepository.save(loginUserEntity);
        } else {
            LoginUserEntity loginUser =
                    LoginUserMapper.MAPPER.toLoginUserEntity(loginUserModelRequest);
            this.loginUserRepository.save(loginUser);
        }
    }

    public LoginUserResponseModel getCurrentLoginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.nonNull(authentication)
                && authentication.getPrincipal() instanceof LoginUserModel principal) {
            Optional<LoginUserEntity> loginUserOpt =
                    this.loginUserRepository.findByEmail(principal.getEmail());
            if (loginUserOpt.isEmpty()) {
                throw new ResourceNotFoundException(
                        MessageFormat.format(LOGIN_USER_NOT_FOUND, principal.getEmail()));
            }
            return LoginUserMapper.MAPPER.toLoginUserResponseModel(loginUserOpt.get());
        }
        throw new BadCredentialException(BAD_CREDENTIAL_EXCEPTION);
    }
}
