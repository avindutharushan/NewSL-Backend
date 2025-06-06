package lk.ijse.newslbackend.controller;

import lk.ijse.newslbackend.customObj.UserResponse;
import lk.ijse.newslbackend.dto.UserResponseDTO;
import lk.ijse.newslbackend.jwtModels.UserRequestDTO;
import lk.ijse.newslbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    /**
     * Endpoint for updating a user.
     *
     * @param userRequestDTO the user data to update
     * @return ResponseEntity with no content status
     */
    @PatchMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateUser(@RequestBody UserRequestDTO userRequestDTO) {
        userRequestDTO.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        userService.updateUser(userRequestDTO);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    /**
     * Endpoint for deleting a user by email.
     *
     * @param email the email of the user to delete
     * @return ResponseEntity with no content status
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteUser(@RequestParam("email") String email) {
        userService.deleteUser(email);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    /**
     * Endpoint for retrieving a user by email.
     *
     * @param username the email of the user to retrieve
     * @return ResponseEntity with the user data and OK status
     */
    @GetMapping
    public ResponseEntity<UserResponse> getUser(@RequestParam("username") String username) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getSelectedUser(username));
    }
    /**
     * Endpoint for retrieving all users.
     *
     * @return ResponseEntity with the list of all users and OK status
     */
    @GetMapping("/all")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAllUsers());
    }
}
