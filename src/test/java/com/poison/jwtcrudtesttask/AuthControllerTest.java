package com.poison.jwtcrudtesttask;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.poison.jwtcrudtesttask.controllers.AuthController;
import com.poison.jwtcrudtesttask.security.jwt.JwtUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;


import com.poison.jwtcrudtesttask.repository.UserRepository;
import com.poison.jwtcrudtesttask.repository.RoleRepository;


import java.util.*;

@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private PasswordEncoder encoder;

    @Test
    public void testSignIn() throws Exception {
        String jwtToken = "mock-jwt-token";  // Имитируем JWT-токен

        when(authenticationManager.authenticate(any())).thenReturn(new UsernamePasswordAuthenticationToken("user", "password"));
        when(jwtUtils.generateJwtToken(any())).thenReturn(jwtToken);

        mockMvc.perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"user\",\"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mock-jwt-token"));
    }

    @Test
    public void testSignUp() throws Exception {
        when(userRepository.existsByUsername("user")).thenReturn(false);  // Имитация отсутствия пользователя
        when(userRepository.existsByEmail("user@example.com")).thenReturn(false);

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"user\",\"email\":\"user@example.com\",\"password\":\"password\",\"role\":[\"user\"]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User registered successfully!"));
    }

    @Test
    public void testSignUpExistingUsername() throws Exception {
        when(userRepository.existsByUsername("user")).thenReturn(true);  // Имитация существующего имени пользователя

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"user\",\"email\":\"user@example.com\",\"password\":\"password\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Error: Username is already taken!"));
    }

    @Test
    public void testSignUpExistingEmail() throws Exception {
        when(userRepository.existsByEmail("user@example.com")).thenReturn(true);  // Имитация существующего email

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"user\",\"email\":\"user@example.com\",\"password\":\"password\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Error: Email is already in use!"));
    }
}
