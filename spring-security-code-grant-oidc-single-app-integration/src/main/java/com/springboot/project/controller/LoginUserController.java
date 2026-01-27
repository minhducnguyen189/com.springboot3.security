package com.springboot.project.controller;

import com.springboot.project.generated.api.LoginUserApi;
import com.springboot.project.generated.model.LoginUserResponseModel;
import com.springboot.project.service.LoginUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("hasAnyAuthority('ADMIN','USER')")
public class LoginUserController implements LoginUserApi {

    private final LoginUserService loginUserService;

    @Autowired
    public LoginUserController(LoginUserService loginUserService) {
        this.loginUserService = loginUserService;
    }

    @Override
    public ResponseEntity<LoginUserResponseModel> getCurrentLoginUser() {
        return new ResponseEntity<>(this.loginUserService.getCurrentLoginUser(), HttpStatus.OK);
    }
}
