package lk.ijse.newslbackend.customObj.impl;

import lk.ijse.newslbackend.customObj.DataPersistResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DataPersistErrorResponse implements DataPersistResponse {
    private int errorCode;
    private String errorMessage;
}