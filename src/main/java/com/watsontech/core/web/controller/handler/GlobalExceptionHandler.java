package com.watsontech.core.web.controller.handler;

import com.watsontech.core.web.result.Result;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Watson on 2019/12/25.
 */
@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result illegalArgumentException(IllegalArgumentException exception, final HttpServletResponse response) {
        return Result.errorBadRequest(exception.getMessage());
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result bindException(BindException exception, final HttpServletResponse response) throws IOException {
        return Result.errorBindErrors(exception.getFieldErrors());
    }

    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result dbDataIntegrityViolationException(org.springframework.dao.DataIntegrityViolationException exception) {
        return Result.errorInternal(exception.getMessage());
    }

    @ExceptionHandler(java.sql.SQLIntegrityConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result dbViolationException(java.sql.SQLIntegrityConstraintViolationException exception) {
        return Result.errorInternal(exception.getMessage());
    }

    @ExceptionHandler(org.springframework.dao.DuplicateKeyException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result dbDuplicateKeyException(org.springframework.dao.DuplicateKeyException exception) {
        return Result.errorInternal("对不起，发现冲突，请更换关键字后重试："+exception.getMessage());
    }

    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result methodArgumentNotValidException(org.springframework.web.bind.MethodArgumentNotValidException exception) {
        return Result.errorBindErrors(exception.getBindingResult().getFieldErrors());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result otherException(Exception exception, final HttpServletResponse response) {
        return Result.errorInternal(exception.getMessage());
    }
}
