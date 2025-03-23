package lk.ijse.newslbackend.customObj.impl;

import lk.ijse.newslbackend.customObj.InternalServerResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class InternalServerErrorResponse implements InternalServerResponse {
    private int errorCode;
    private String errorMessage;
}