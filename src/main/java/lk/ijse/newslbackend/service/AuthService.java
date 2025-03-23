package lk.ijse.newslbackend.service;

import lk.ijse.newslbackend.jwtModels.JwtAuthResponse;
import lk.ijse.newslbackend.jwtModels.UserRequestDTO;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface AuthService {
    JwtAuthResponse signIn(UserRequestDTO signIn);
    JwtAuthResponse signUp(UserRequestDTO signUp);
    JwtAuthResponse refreshToken(String accessToken);
    void verifyUserEmail(String option, String email, String username);
}