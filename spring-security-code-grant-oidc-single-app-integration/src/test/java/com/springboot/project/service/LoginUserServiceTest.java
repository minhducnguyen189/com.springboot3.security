package com.springboot.project.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.springboot.project.entity.LoginUserEntity;
import com.springboot.project.exception.BadCredentialException;
import com.springboot.project.exception.ResourceNotFoundException;
import com.springboot.project.generated.model.LoginUserResponseModel;
import com.springboot.project.model.LoginUserModel;
import com.springboot.project.repository.LoginUserRepository;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
class LoginUserServiceTest {

    @Mock private LoginUserRepository loginUserRepository;

    @InjectMocks private LoginUserService loginUserService;

    @Mock private SecurityContext securityContext;

    @Mock private Authentication authentication;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void upsert_login_user_create_new() {
        // Given
        LoginUserModel request = new LoginUserModel();
        request.setEmail("test@example.com");

        when(loginUserRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        // When
        loginUserService.upsertLoginUser(request);

        // Then
        verify(loginUserRepository).save(any(LoginUserEntity.class));
    }

    @Test
    void upsert_login_user_update_existing() {
        // Given
        LoginUserModel request = new LoginUserModel();
        request.setEmail("test@example.com");

        LoginUserEntity existing = new LoginUserEntity();
        existing.setEmail("test@example.com");

        when(loginUserRepository.findByEmail("test@example.com")).thenReturn(Optional.of(existing));

        // When
        loginUserService.upsertLoginUser(request);

        // Then
        verify(loginUserRepository).save(existing);
    }

    @Test
    void get_current_login_user_success() {
        // Given
        LoginUserModel principal = new LoginUserModel();
        principal.setEmail("test@example.com");

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(principal);

        LoginUserEntity entity = new LoginUserEntity();
        entity.setEmail("test@example.com");
        entity.setUsername("testuser");

        when(loginUserRepository.findByEmail("test@example.com")).thenReturn(Optional.of(entity));

        // When
        LoginUserResponseModel response = loginUserService.getCurrentLoginUser();

        // Then
        assertNotNull(response);
        assertEquals("test@example.com", response.getEmail());
        assertEquals("testuser", response.getUsername());
    }

    @Test
    void get_current_login_user_not_found() {
        // Given
        LoginUserModel principal = new LoginUserModel();
        principal.setEmail("test@example.com");

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(principal);

        when(loginUserRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        // When & Then
        assertThrows(
                ResourceNotFoundException.class, () -> loginUserService.getCurrentLoginUser());
    }

    @Test
    void get_current_login_user_bad_credential() {
        // Given
        when(securityContext.getAuthentication()).thenReturn(null);

        // When & Then
        assertThrows(
                BadCredentialException.class, () -> loginUserService.getCurrentLoginUser());
    }
}
