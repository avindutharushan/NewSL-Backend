package lk.ijse.newslbackend.service.impl;


import lk.ijse.newslbackend.customObj.impl.MailBody;
import lk.ijse.newslbackend.entity.User;
import lk.ijse.newslbackend.exception.*;
import lk.ijse.newslbackend.jwtModels.JwtAuthResponse;
import lk.ijse.newslbackend.jwtModels.UserRequestDTO;
import lk.ijse.newslbackend.repository.UserRepository;
import lk.ijse.newslbackend.service.AuthService;
import lk.ijse.newslbackend.service.JwtService;
import lk.ijse.newslbackend.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * This class was created to handle authentication related services
 * AuthService Implementation
 * @author - Gayanuka Bulegoda
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final Mapping mapping;
    private final AuthenticationManager authenticationManager;
    private final EmailUtil emailUtil;
    private final OtpManager otpManager;

    @Override
    public JwtAuthResponse signIn(UserRequestDTO signIn) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(signIn.getEmail(), signIn.getPassword()));
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid credentials", e);
        }
        var userByEmail = userRepository.findByEmail(signIn.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        try {
            var generatedToken = jwtService.generateToken(userByEmail);
            return JwtAuthResponse
                    .builder()
                    .username(userByEmail.getEmail())
                    .token(generatedToken)
                    .role(userByEmail.getRole().getRoleByString())
                    .build();
        } catch (Exception e) {
            throw new JwtAuthenticationException("Failed to generate JWT token", e);
        }
    }

    @Override
    public JwtAuthResponse signUp(UserRequestDTO signUpUser){
        userRepository.findByEmail(signUpUser.getEmail())
                .ifPresent(user -> {
                    throw new DataPersistFailedException("Email already exists", 2);
                });
        userRepository.findByUsername(signUpUser.getUsername())
                .ifPresent(user -> {
                    throw new DataPersistFailedException("Username already exists", 2);
                });
        String role = signUpUser.getRole();
        if (role == null || (role.equals("ADMIN")))
            throw new DataPersistFailedException("Role not found or not authorized", 3);
        if (otpManager.validateOtp(signUpUser.getEmail(), signUpUser.getOtp())) {
            otpManager.removeOtp(signUpUser.getEmail());
        } else throw new InvalidOtpException("Invalid OTP");
        User user = mapping.convertToEntity(signUpUser, User.class);

        String imageUrl = null;
        try {
            imageUrl = ImageUploadUtil.saveFile(signUpUser.getUsername(), "profile", signUpUser.getProfilePicture());
        } catch (Exception e) {
            throw new FileConversionException("Cannot save file");
        }
        user.setProfilePicture(imageUrl);

        try {
            User savedUser = userRepository.save(user);
            var generatedToken = jwtService.generateToken(savedUser);
            return JwtAuthResponse
                    .builder()
                    .username(savedUser.getEmail())
                    .token(generatedToken)
                    .role(savedUser.getRole().getRoleByString())
                    .build();
        } catch (Exception e) {
            throw new DataPersistFailedException("Cannot Save User", 0, e);
        }
    }


    @Override
    public JwtAuthResponse refreshToken(String accessToken) {
        var email = jwtService.extractUsername(accessToken);
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        var refreshToken = jwtService.refreshToken(user);
        return JwtAuthResponse
                .builder()
                .username(user.getEmail())
                .token(refreshToken)
                .role(user.getRole().getRoleByString())
                .build();
    }

    @Override
    public void verifyUserEmail(String option, String email, String username) {
        userRepository.findByEmail(email)
                .ifPresent(user -> {
                    throw new DataPersistFailedException("Email already exists", 2);
                });
        userRepository.findByUsername(username)
                .ifPresent(user -> {
                    throw new DataPersistFailedException("Username already exists", 2);
                });
        String subject;
        String templateName;
        if (Objects.equals(option, "0")) {
            subject = "Verify Your Email";
            templateName = "email-verification";
        } else if (Objects.equals(option, "1")) {
            subject = "Password Reset Request";
            templateName = "password-reset";
        } else throw new EmailVerificationException("Invalid option");
        String otp = emailUtil.otpGenerator().toString();
        Map<String, Object> map = Map.of("name", username, "otp", otp); // returns an unmodifiable map
        emailUtil.sendHtmlMessage(
                MailBody.builder()
                        .to(email)
                        .subject(subject)
                        .templateName(templateName)
                        .replacements(map)
                        .build()
        );
        otpManager.storeOtp(email, otp);
    }
}