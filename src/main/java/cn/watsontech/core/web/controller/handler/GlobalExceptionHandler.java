package cn.watsontech.core.web.controller.handler;

import cn.watsontech.core.web.result.Result;
import lombok.extern.log4j.Log4j2;
import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
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
        if(log.isDebugEnabled()) {
            return Result.errorBadRequest(exception.getMessage());
        }else {
            return Result.errorBadRequest(exception.getMessage());
        }
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result bindException(BindException exception, final HttpServletResponse response) throws IOException {
        return Result.errorBindErrors(exception.getFieldErrors());
    }

    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result dbDataIntegrityViolationException(org.springframework.dao.DataIntegrityViolationException exception) {
        if(log.isDebugEnabled()) {
            return Result.errorInternal(exception.getMessage());
        }else {
            return Result.errorInternal("对不起，发现数据冲突，请稍后再试");
        }
    }

    @ExceptionHandler(java.sql.SQLIntegrityConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result dbViolationException(java.sql.SQLIntegrityConstraintViolationException exception) {
        if(log.isDebugEnabled()) {
            return Result.errorInternal(exception.getMessage());
        }else {
            return Result.errorInternal("对不起，外键约束失败导致插入或更新数据出错，请稍后再试");
        }
    }

    @ExceptionHandler(org.springframework.dao.DuplicateKeyException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result dbDuplicateKeyException(org.springframework.dao.DuplicateKeyException exception) {
        if(log.isDebugEnabled()) {
            return Result.errorInternal("对不起，发现数据冲突，请更换关键字后重试："+exception.getMessage());
        }else {
            return Result.errorInternal("对不起，发现数据冲突，请更换关键字后重试");
        }
    }

    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result methodArgumentNotValidException(org.springframework.web.bind.MethodArgumentNotValidException exception) {
        return Result.errorBindErrors(exception.getBindingResult().getFieldErrors());
    }

    @ExceptionHandler(PersistenceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result persistenceException(PersistenceException exception, final HttpServletResponse response) {
        if(log.isDebugEnabled()) {
            return Result.errorInternal(exception.getMessage());
        }else {
            return Result.errorInternal("对不起，服务器(数据访问)出错，请稍后再试");
        }
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result authenticationException(AuthenticationException exception, final HttpServletResponse response) {
        return Result.errorInternal(exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result otherException(Exception exception, final HttpServletResponse response) {
        if(log.isDebugEnabled()) {
            return Result.errorInternal(exception.getMessage());
        }else {
            return Result.errorInternal("对不起，服务器出错，请稍后再试");
        }
    }
}