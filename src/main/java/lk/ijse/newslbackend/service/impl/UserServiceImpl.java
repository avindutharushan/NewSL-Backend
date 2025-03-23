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
 * @author Gayanuka Bulegoda
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
        /*Staff staff = staffRepository.findByEmailAndStatusNot(user.getEmail(), Status.REMOVED)
                .orElseThrow(() -> new StaffNotFoundException("User not found in staff"));
        userRepository.delete(user);
        Map<String, Object> map = Map.of("name", staff.getFirstName() + " " + staff.getLastName());*/
        emailUtil.sendHtmlMessage(
                MailBody.builder()
                        .to(email)
                        .subject("Account Deactivation")
                        .templateName("account-deactivation")
                        .replacements(null)//map
                        .build()
        );
    }

    @Override
    public UserResponse getSelectedUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        UserResponseDTO userResponseDTO = mapping.convertToDTO(user, UserResponseDTO.class);
        /*Staff staff = staffRepository.findByEmailAndStatusNot(user.getEmail(), Status.REMOVED)
                .orElseThrow(() -> new StaffNotFoundException("User not found in staff"));
        userResponseDTO.setName(staff.getFirstName() + " " + staff.getLastName());
        userResponseDTO.setGender(String.valueOf(staff.getGender()));*/
        return userResponseDTO;
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) throw new UserNotFoundException("No users found");
        List<UserResponseDTO> userResponseDTOS = mapping.convertToDTOList(users, UserResponseDTO.class);
        for (UserResponseDTO userResponseDTO : userResponseDTOS) {
           /* Staff staff = staffRepository.findByEmailAndStatusNot(userResponseDTO.getEmail(), Status.REMOVED)
                    .orElseThrow(() -> new StaffNotFoundException("User not found in staff"));
            userResponseDTO.setName(staff.getFirstName() + " " + staff.getLastName());*/
        }
        return userResponseDTOS;
    }

    @Override
    public UserDetailsService userDetailsService() {
        return email -> userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }
}