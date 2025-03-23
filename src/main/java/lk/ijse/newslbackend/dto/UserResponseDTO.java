package lk.ijse.newslbackend.dto;

import lk.ijse.newslbackend.customObj.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserResponseDTO implements UserResponse {
    private String username;
    private String email;
    private String role;
    private MultipartFile profilePicture;
}