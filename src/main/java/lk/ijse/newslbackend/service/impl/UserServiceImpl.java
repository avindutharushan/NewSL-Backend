package lk.ijse.newslbackend.service.impl;

import lk.ijse.newslbackend.customObj.UserResponse;
import lk.ijse.newslbackend.customObj.impl.MailBody;
import lk.ijse.newslbackend.dto.UserResponseDTO;
import lk.ijse.newslbackend.entity.User;
import lk.ijse.newslbackend.exception.InvalidOtpException;
import lk.ijse.newslbackend.exception.UserNotFoundException;
import lk.ijse.newslbackend.jwtModels.UserRequestDTO;
import lk.ijse.newslbackend.repository.UserRepository;
import lk.ijse.newslbackend.service.UserService;
import lk.ijse.newslbackend.util.EmailUtil;
import lk.ijse.newslbackend.util.ImageUploadUtil;
import lk.ijse.newslbackend.util.Mapping;
import lk.ijse.newslbackend.util.OtpManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
/**
 * This class was created to implement the business logic of the User
 * Service Implementation
 *
 * @author Avindu Tharushan
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final Mapping mapping;
    private final EmailUtil emailUtil;
    private final OtpManager otpManager;

    @Override
    @Transactional
    public void updateUser(UserRequestDTO userRequestDTO) {
        User user = userRepository.findByEmail(userRequestDTO.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + userRequestDTO.getEmail() + " not found"));
        if (userRequestDTO.getOtp() == null) {
            user.setPassword(userRequestDTO.getPassword());
        } else if (otpManager.validateOtp(userRequestDTO.getEmail(), userRequestDTO.getOtp())) {
            otpManager.removeOtp(userRequestDTO.getEmail());
            user.setPassword(userRequestDTO.getPassword());
        } else throw new InvalidOtpException("Invalid OTP");
    }


    @Override
    public void deleteUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        userRepository.delete(user);
        Map<String, Object> map = Map.of("name", user.getUsername());
        emailUtil.sendHtmlMessage(
                MailBody.builder()
                        .to(email)
                        .subject("Account Deactivation")
                        .templateName("account-deactivation")
                        .replacements(map)
                        .build()
        );
    }

    @Override
    @Transactional
    public UserResponseDTO getSelectedUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        UserResponseDTO userResponseDTO = mapping.convertToDTO(user, UserResponseDTO.class);
        userResponseDTO.setUsername(username);
        userResponseDTO.setProfilePicture(user.getProfilePicture());
        return userResponseDTO;
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) throw new UserNotFoundException("No users found");
        List<UserResponseDTO> userResponseDTOS = mapping.convertToDTOList(users, UserResponseDTO.class);
        return userResponseDTOS;
    }

    @Override
    public UserDetailsService userDetailsService() {
        return email -> userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }
}