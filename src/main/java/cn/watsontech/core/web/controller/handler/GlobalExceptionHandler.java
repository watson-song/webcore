package cn.watsontech.core.web.controller.handler;

import cn.watsontech.core.utils.HttpUtils;
import cn.watsontech.core.web.result.Result;
import cn.watsontech.core.web.spring.security.LoginUser;
import lombok.extern.log4j.Log4j2;
import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by Watson on 2019/12/25.
 */
@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result illegalArgumentException(IllegalArgumentException exception, final HttpServletRequest request) {
        insertErrorLog(request, exception, "参数不合法异常illegalArgumentException，返回BAD_REQUEST");

        if(log.isDebugEnabled()) {
            return Result.errorBadRequest(exception.getMessage());
        }else {
            return Result.errorBadRequest(exception.getMessage());
        }
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result bindException(BindException exception, final HttpServletRequest request) {
        insertErrorLog(request, exception, "参数绑定异常BindException，返回BAD_REQUEST");

        return Result.errorBindErrors(exception.getFieldErrors());
    }

    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result dbDataIntegrityViolationException(org.springframework.dao.DataIntegrityViolationException exception, final HttpServletRequest request) {
        insertErrorLog(request, exception, "数据库异常DataIntegrityViolationException，返回BAD_REQUEST");

        if(log.isDebugEnabled()) {
            return Result.errorInternal(exception.getMessage());
        }else {
            return Result.errorInternal("对不起，发现数据冲突，请稍后再试");
        }
    }

    @ExceptionHandler(java.sql.SQLIntegrityConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result dbViolationException(java.sql.SQLIntegrityConstraintViolationException exception, final HttpServletRequest request) {
        insertErrorLog(request, exception, "数据库验证异常SQLIntegrityConstraintViolationException，返回BAD_REQUEST");

        if(log.isDebugEnabled()) {
            return Result.errorInternal(exception.getMessage());
        }else {
            return Result.errorInternal("对不起，外键约束失败导致插入或更新数据出错，请稍后再试");
        }
    }

    @ExceptionHandler(org.springframework.dao.DuplicateKeyException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result dbDuplicateKeyException(org.springframework.dao.DuplicateKeyException exception, final HttpServletRequest request) {
        insertErrorLog(request, exception, "数据库冲突异常DuplicateKeyException，返回BAD_REQUEST");

        if(log.isDebugEnabled()) {
            return Result.errorInternal("对不起，发现数据冲突，请更换关键字后重试："+exception.getMessage());
        }else {
            return Result.errorInternal("对不起，发现数据冲突，请更换关键字后重试");
        }
    }

    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result methodArgumentNotValidException(org.springframework.web.bind.MethodArgumentNotValidException exception, final HttpServletRequest request) {
        insertErrorLog(request, exception, "方法参数验证异常MethodArgumentNotValidException，返回BAD_REQUEST");

        return Result.errorBindErrors(exception.getBindingResult().getFieldErrors());
    }

    @ExceptionHandler(PersistenceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result persistenceException(PersistenceException exception, final HttpServletRequest request) {
        insertErrorLog(request, exception, "数据库存储异常PersistenceException，返回INTERNAL_SERVER_ERROR");

        if(log.isDebugEnabled()) {
            return Result.errorInternal(exception.getMessage());
        }else {
            return Result.errorInternal("对不起，服务器(数据访问)出错，请稍后再试");
        }
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result authenticationException(AuthenticationException exception, final HttpServletRequest request) {
        insertErrorLog(request, exception, "授权失败异常AuthenticationException，返回INTERNAL_SERVER_ERROR");
        return Result.errorInternal(exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result otherException(Exception exception, final HttpServletRequest request) {
        insertErrorLog(request, exception, "未知异常Exception，返回INTERNAL_SERVER_ERROR");

        if(log.isDebugEnabled()) {
            return Result.errorInternal(exception.getMessage());
        }else {
            return Result.errorInternal("对不起，服务器出错，请稍后再试");
        }
    }

    private void insertErrorLog(HttpServletRequest request, Exception ex, String message) {
        if (jdbcTemplate!=null) {
            try {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                String userName = "";
                Long userId = null;
                if (authentication!=null) {
                    Object principle = authentication.getPrincipal();
                    if (principle!=null) {
                        if (principle instanceof LoginUser) {
                            LoginUser user = (LoginUser)principle;
                            if (user!=null) {
                                userId = user.getId();
                                userName = user.getFullName();
                            }
                        }else {
                            userName = principle.toString();
                        }
                    }
                }

                Object[] args = new Object[]{"error", message, "ip", "method", userId, userName, "url", "params", ex.getMessage(), 0, 0};
                fillRequestParams(request, args);
                jdbcTemplate.update("INSERT INTO `tb_access_log` (`level`, `title`, `ip`, `method`, `created_by`, `created_by_name`, `url`, `params`, `exception`, `total_times`, `db_times`) VALUES (?,?,?,?,?,?,?,?,?,?,?)", args);
            }catch (Exception e) {
                log.error("打印报错日志：{} 发生错误：{}", ex, e);
            }
        }
    }

    private void fillRequestParams(HttpServletRequest request, Object[] args) {
        if (request!=null) {
            String remoteAddr = HttpUtils.getRealIp(request);// 请求的IP
            String requestUri = request.getRequestURI();// 请求的Uri
            String method = request.getMethod(); // 请求的方法类型(post/get)
            Map<String, String[]> params = request.getParameterMap(); // 请求提交的参数

            args[2] = remoteAddr;
            args[3] = method;
            args[5] = requestUri;
            args[6] = cn.watsontech.core.web.spring.util.StringUtils.mapToQueryString(params);
        }
    }
}