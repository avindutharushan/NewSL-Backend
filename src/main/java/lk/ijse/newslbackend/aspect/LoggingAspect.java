package lk.ijse.newslbackend.aspect;

import lk.ijse.newslbackend.jwtModels.JwtAuthResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * This aspect is used to log the method name and arguments of all controller methods.
 * The aspect is divided into two parts: pointcuts and advices.
 * Pointcuts are used to define the methods that need to be logged.
 * Advices are used to log the method name and arguments before and after the method is executed.
 */
@Aspect
@Component
@Slf4j
public class LoggingAspect {
    /**
     * Pointcuts for all controller methods.
     */
    @Pointcut("execution(* lk.ijse.newslbackend.controller.UserController.*(..))")
    public void userControllerMethods() {}
    @Pointcut("execution(* lk.ijse.newslbackend.controller.AuthController.*(..))")
    public void authControllerMethods() {}
    @Pointcut("execution(* lk.ijse.newslbackend.controller.HealthTestController.*(..))")
    public void healthTestControllerMethods() {}
    /**
     * Advice for logging before the method is executed.
     * @param joinPoint the method that is being executed.
     */
    @Before("authControllerMethods() || healthTestControllerMethods() || userControllerMethods()")
    private void logBefore(JoinPoint joinPoint) {
        log.info("Entering method: {} with arguments: {}", joinPoint.getSignature().toShortString(), joinPoint.getArgs());
    }
    /**
     * Advice for logging after the method is executed.
     * @param joinPoint the method that is being executed.
     * @param result the return value of the method.
     */
    @AfterReturning(pointcut = "authControllerMethods() || healthTestControllerMethods() || userControllerMethods()", returning = "result")
    private void logAfterReturning(JoinPoint joinPoint, Object result) {
        if (result instanceof ResponseEntity<?> responseEntity) {
            logResponseEntity(joinPoint, responseEntity);
        } else if (result instanceof Collection<?> collection) {
            logCollection(joinPoint, collection);
        } else {
            logResult(joinPoint, result);
        }
    }
    /**
     * Logs the response entity.
     * @param joinPoint the method that is being executed.
     * @param responseEntity the response entity.
     */
    private void logResponseEntity(JoinPoint joinPoint, ResponseEntity<?> responseEntity) {
        Object body = responseEntity.getBody();
        if (body instanceof JwtAuthResponse) {
            log.info("Exiting method: {} with result: <{} {} {}>",
                    joinPoint.getSignature().toShortString(),
                    responseEntity.getStatusCode(),
                    ((HttpStatus) responseEntity.getStatusCode()).getReasonPhrase(),
                    "JwtAuthResponse(token=****)");
        } else if (body instanceof Collection<?> collection) {
            logCollection(joinPoint, collection);
        } else {
            logResult(joinPoint, body);
        }
    }
    /**
     * Logs the collection.
     * @param joinPoint the method that is being executed.
     * @param collection the collection.
     */
    private void logCollection(JoinPoint joinPoint, Collection<?> collection) {
        log.info("Exiting method: {} with result size: {}", joinPoint.getSignature().toShortString(), collection.size());
    }
    /**
     * Logs the result.
     * @param joinPoint the method that is being executed.
     * @param result the result.
     */
    private void logResult(JoinPoint joinPoint, Object result) {
        log.info("Exiting method: {} with result: {}", joinPoint.getSignature().toShortString(), result);
    }
}