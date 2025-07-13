package com.ecommerce.project.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ecommerce.project.payload.ApiResponse;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class MyGlobalExceptionHandler {
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
		Map<String,String> response = new HashMap<>();
		e.getBindingResult().getFieldErrors().forEach(error->{
			String defaultMessage = error.getDefaultMessage();
			 String fieldName = error.getField();
			response.put(fieldName ,defaultMessage );
		});
		return new ResponseEntity<Map<String,String>>(response,HttpStatus.BAD_REQUEST);
	}
	
	
	 @ExceptionHandler(ConstraintViolationException.class)
	    public ResponseEntity<Map<String, String>> handleConstraintViolation(ConstraintViolationException e) {
	        Map<String, String> errors = new HashMap<>();
	        e.getConstraintViolations().forEach(violation -> {
	            String field = violation.getPropertyPath().toString();
            String message = violation.getMessage();
	            errors.put(field, message);
	        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	    }
	
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiResponse> myResourceNotFoundException(ResourceNotFoundException e){
		String message = e.getMessage();
		ApiResponse apiResponse= new ApiResponse(message,false);
		return new ResponseEntity<>(apiResponse,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(ApiException.class)
	public ResponseEntity<ApiResponse> myApiException(ApiException e){
		String message = e.getMessage();
		ApiResponse apiResponse= new ApiResponse(message,false);
		return new ResponseEntity<>(apiResponse,HttpStatus.BAD_REQUEST);
	}
	
}
