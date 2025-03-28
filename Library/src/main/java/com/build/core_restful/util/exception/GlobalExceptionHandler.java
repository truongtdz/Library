package com.build.core_restful.util.exception;

import com.build.core_restful.domain.response.FormatResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // Handle all exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<FormatResponse<Object>> handleAllException(Exception ex) {
        FormatResponse<Object> res = new FormatResponse<Object>();
        res.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        res.setMessage(ex.getMessage());
        res.setError("Internal Server Error");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
    }

    // Handle exceptions often encountered
    @ExceptionHandler(value = {
            UsernameNotFoundException.class,
            BadCredentialsException.class,
            NewException.class
    })
    public ResponseEntity<FormatResponse<Object>> handleException(Exception ex){
        FormatResponse<Object> res = new FormatResponse<Object>();

        res.setCode(HttpStatus.BAD_REQUEST.value());
        res.setError(ex.getMessage());
        res.setMessage("Call api fail, details in error");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    // Handle exception URL not found
    @ExceptionHandler(value = NoResourceFoundException.class)
    public ResponseEntity<FormatResponse<Object>> handleNotFoundException(Exception ex){
        FormatResponse<Object> res = new FormatResponse<Object>();

        res.setCode(HttpStatus.BAD_REQUEST.value());
        res.setError(ex.getMessage());
        res.setMessage("URL not found");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    // Handle exception Not Valid
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<FormatResponse<Object>> handleNotValid(MethodArgumentNotValidException ex){
        BindingResult result =ex.getBindingResult();

        final List<FieldError> fieldErrors = result.getFieldErrors();

        FormatResponse<Object> res = new FormatResponse<Object>();

        res.setCode(HttpStatus.BAD_REQUEST.value());
        res.setError(ex.getBody().getDetail());

        List<String> errors = fieldErrors.stream().map(f -> f.getDefaultMessage()).collect(Collectors.toUnmodifiableList());
        res.setMessage(errors.size() > 1 ? errors : errors.get(0));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }
}
