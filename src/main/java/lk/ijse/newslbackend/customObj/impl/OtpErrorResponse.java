package lk.ijse.newslbackend.customObj.impl;

import lk.ijse.newslbackend.customObj.OtpResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OtpErrorResponse implements OtpResponse {
    private int errorCode;
    private String errorMessage;
}