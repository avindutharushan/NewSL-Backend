package lk.ijse.newslbackend.service;


import lk.ijse.newslbackend.customObj.UserResponse;
import lk.ijse.newslbackend.dto.UserResponseDTO;
import lk.ijse.newslbackend.jwtModels.UserRequestDTO;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    void updateUser(UserRequestDTO userRequestDTO);
    void deleteUser(String email);
    UserResponse getSelectedUser(String email);
    List<UserResponseDTO> getAllUsers();
    UserDetailsService userDetailsService();
}