package lk.ijse.newslbackend.customObj.impl;

import lk.ijse.newslbackend.customObj.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserErrorResponse implements UserResponse {
    private int errorCode;
    private String errorMessage;
}