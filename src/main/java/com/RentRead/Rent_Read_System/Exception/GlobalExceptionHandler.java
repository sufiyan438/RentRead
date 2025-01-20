package com.RentRead.Rent_Read_System.Exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.RentRead.Rent_Read_System.Controller.AuthController;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    private Logger logger = LoggerFactory.getLogger(AuthController.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> globalExceptionHandler(Exception ex){
        logger.error("Exception occured!");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("It can be one of the following errors:\n" + 
                                                                                "- Incorrect info fed to the system.\n" + 
                                                                                "- Incorrect role assigned\n" +
                                                                                "- Wrong url");
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<?> resourceAlreadyExistsException(ResourceAlreadyExistsException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }
}
