package lk.ijse.newslbackend.exception.advice;

import lk.ijse.newslbackend.customObj.*;
import lk.ijse.newslbackend.customObj.impl.*;
import lk.ijse.newslbackend.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is used to handle all the exceptions that are thrown in the application.
 * This class is annotated with @ControllerAdvice to indicate that this class is used to handle exceptions globally.
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    /**
     * This method is used to handle the exceptions that are thrown when the validation of the request body fails.
     * @param ex MethodArgumentNotValidException object
     * @return ResponseEntity object with a map of error messages and the status code
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
    /**
     * This method is used to handle the exceptions that are thrown when the file conversion fails.
     * @param e FileConversionException object
     * @return ResponseEntity object with a FileResponse object and the status code
     */
    @ExceptionHandler(FileConversionException.class)
    public ResponseEntity<FileResponse> handleFileConversionException(FileConversionException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new FileErrorResponse(0, e.getMessage()));
    }
    /**
     * This method is used to handle the exceptions that are thrown when the data persist fails.
     * @param e DataPersistFailedException object
     * @return ResponseEntity object with a DataPersistResponse object and the status code
     */
    @ExceptionHandler(DataPersistFailedException.class)
    public ResponseEntity<DataPersistResponse> handleDataPersistFailedException(DataPersistFailedException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new DataPersistErrorResponse(e.getErrorCode(), e.getMessage()));
    }
    /**
     * This method is used to handle the exceptions that are thrown when the user is not found.
     * @param e UserNotFoundException object
     * @return ResponseEntity object with a UserResponse object and the status code
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<UserResponse> handleUserNotFoundException(UserNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new UserErrorResponse(0, e.getMessage()));
    }
    /**
     * This method is used to handle the exceptions that are thrown when the username is not found.
     * @param e UsernameNotFoundException object
     * @return ResponseEntity object with a UserResponse object and the status code
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<UserResponse> handleUsernameNotFoundException(UsernameNotFoundException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new UserErrorResponse(0, e.getMessage()));
    }
    /**
     * This method is used to handle the exceptions that are thrown when the JWT authentication fails.
     * @param e JwtAuthenticationException object
     * @return ResponseEntity object with a UserResponse object and the status code
     */
    @ExceptionHandler(JwtAuthenticationException.class)
    public ResponseEntity<UserResponse> handleJwtAuthenticationException(JwtAuthenticationException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new UserErrorResponse(0, e.getMessage()));
    }
    /**
     * This method is used to handle the exceptions that are thrown when the bad credentials are provided.
     * @param e BadCredentialsException object
     * @return ResponseEntity object with a UserResponse object and the status code
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<UserResponse> handleBadCredentialsException(BadCredentialsException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new UserErrorResponse(0, e.getMessage()));
    }
    /**
     * This method is used to handle the exceptions that are thrown when the invalid OTP is provided.
     * @param e InvalidOtpException object
     * @return ResponseEntity object with an OtpResponse object and the status code
     */
    @ExceptionHandler(InvalidOtpException.class)
    public ResponseEntity<OtpResponse> handleInvalidOtpException(InvalidOtpException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new OtpErrorResponse(0, e.getMessage()));
    }
    /**
     * This method is used to handle the exceptions that are thrown when the email verification fails.
     * @param e EmailVerificationException object
     * @return ResponseEntity object with a UserResponse object and the status code
     */
    @ExceptionHandler(EmailVerificationException.class)
    public ResponseEntity<UserResponse> handleEmailVerificationException(EmailVerificationException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new UserErrorResponse(0, e.getMessage()));
    }
    /**
     * This method is used to handle general exceptions that are not specifically handled by other methods.
     * @param e Exception object
     * @return ResponseEntity object with an InternalServerResponse object and the status code
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<InternalServerResponse> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new InternalServerErrorResponse(0, e.getMessage()));
    }
}