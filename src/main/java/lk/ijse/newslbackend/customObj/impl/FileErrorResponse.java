package lk.ijse.newslbackend.customObj.impl;

import lk.ijse.newslbackend.customObj.FileResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FileErrorResponse implements FileResponse {
    private int errorCode;
    private String errorMessage;
}