package com.e107.backend.geChu.security.handler;

import com.e107.backend.geChu.security.dto.ErrorDto;
import com.e107.backend.geChu.security.jwt.JwtFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class MethodArgumentNotValidExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorDto methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<org.springframework.validation.FieldError> fieldErrors = result.getFieldErrors();
        return processFieldErrors(fieldErrors);
    }

    private ErrorDto processFieldErrors(List<org.springframework.validation.FieldError> fieldErrors) {
        ErrorDto errorDTO = new ErrorDto(BAD_REQUEST.value(), "@Valid Error");
        for (org.springframework.validation.FieldError fieldError: fieldErrors) {
            errorDTO.addFieldError(fieldError.getObjectName(), fieldError.getField(), fieldError.getDefaultMessage());
        }
        return errorDTO;
    }
}