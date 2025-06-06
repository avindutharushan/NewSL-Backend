package lk.ijse.newslbackend.controller;

import jakarta.validation.Valid;
import lk.ijse.newslbackend.customObj.OtpResponse;
import lk.ijse.newslbackend.customObj.UserResponse;
import lk.ijse.newslbackend.dto.UserResponseDTO;
import lk.ijse.newslbackend.jwtModels.JwtAuthResponse;
import lk.ijse.newslbackend.jwtModels.UserRequestDTO;
import lk.ijse.newslbackend.service.AuthService;
import lk.ijse.newslbackend.service.JwtService;
import lk.ijse.newslbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * AuthController class (REST Controller)
 * This class handles the authentication related requests.
 * It provides endpoints for user sign-up, sign-in, and token refresh.
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserService userService;

    /**
     * Endpoint for user registration.
     *
     * @param userRequestDTO the user registration request data
     * @return ResponseEntity containing the JWT authentication response
     */
    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<JwtAuthResponse> signUp(@Valid @ModelAttribute UserRequestDTO userRequestDTO) {
        userRequestDTO.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.signUp(userRequestDTO));
    }
    /**
     * Endpoint for user login.
     *
     * @param userRequestDTO the user login request data
     * @return ResponseEntity containing the JWT authentication response
     */
    @PostMapping(value = "/authenticate", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JwtAuthResponse> signIn(@RequestBody UserRequestDTO userRequestDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.signIn(userRequestDTO));
    }
    /**
     * Endpoint for validating a user token.
     * @param token the JWT token
     * @param username the email of the user
     * @return ResponseEntity with the validation status
     */
    @PostMapping("/validate")
    public ResponseEntity<Boolean> validateUser(@RequestHeader("Authorization") String token,@RequestParam("username") String username){
        String extracted = jwtService.extractUsername(token.substring(7));
        UserResponseDTO selectedUser = userService.getSelectedUser(username);
        return ResponseEntity.status(HttpStatus.OK).body(extracted.equals(selectedUser.getEmail()));
    }
    /**
     * Endpoint for requesting an OTP.
     *
     * @param option the option to verify the user
     * @param email the email of the user
     * @param username the username of the user
     * @return ResponseEntity with the OTP response and OK status
     */
    @PostMapping("/otp")
    public ResponseEntity<OtpResponse> requestOtp(
            @RequestParam("option") String option, @RequestParam("email") String email,@RequestParam("username") String username)  {
        authService.verifyUserEmail(option, email,username);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    /**
     * Endpoint for refreshing the JWT token.
     *
     * @param refreshToken the refresh token
     * @return ResponseEntity containing the new JWT authentication response
     */
    @PostMapping("/refresh")
    public ResponseEntity<JwtAuthResponse> refreshToken(@RequestParam("refreshToken") String refreshToken) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.refreshToken(refreshToken));
    }
}