package cn.watsontech.webhelper.common.controller.handler;

import cn.watsontech.webhelper.common.result.Result;
import cn.watsontech.webhelper.common.security.LoginUser;
import cn.watsontech.webhelper.common.util.HttpUtils;
import cn.watsontech.webhelper.utils.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by Watson on 2019/12/25.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    protected static final Log log = LogFactory.getLog(GlobalExceptionHandler.class);

    public interface ErrorSaveService {

        /**
         * 保存错误日志
         * 插入参数列表：
         * INSERT INTO `tb_access_log` (`level`, `title`, `ip`, `method`, `created_by`, `created_by_name`, `url`, `params`, `exception`, `total_times`, `db_times`) VALUES (?,?,?,?,?,?,?,?,?,?,?)
         *
         * @param args 参数
         */
        int saveError(Object[] args);
    }

    @Autowired
    ErrorSaveService jdbcTemplate;

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result illegalArgumentException(IllegalArgumentException exception, final HttpServletRequest request) {
        insertErrorLog(request, exception, "参数不合法异常，返回BAD_REQUEST");

        if(log.isDebugEnabled()) {
            return Result.errorBadRequest(exception.getMessage());
        }else {
            return Result.errorBadRequest(exception.getMessage());
        }
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result requestParameterException(MissingServletRequestParameterException exception, final HttpServletRequest request) {
        insertErrorLog(request, exception, "请求缺少参数异常，返回BAD_REQUEST");

        if(log.isDebugEnabled()) {
            return Result.errorBadRequest(exception.getMessage());
        }else {
            return Result.errorBadRequest(String.format("参数：%s必传", exception.getParameterName()));
        }
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result bindException(BindException exception, final HttpServletRequest request) {
        insertErrorLog(request, exception, "参数绑定异常，返回BAD_REQUEST");

        return Result.errorBindErrors(exception.getFieldErrors());
    }

    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result dbDataIntegrityViolationException(org.springframework.dao.DataIntegrityViolationException exception, final HttpServletRequest request) {
        insertErrorLog(request, exception, "数据库异常，返回BAD_REQUEST");

        if(log.isDebugEnabled()) {
            return Result.errorInternal(exception.getMessage());
        }else {
            return Result.errorInternal("对不起，发现数据冲突，请稍后再试");
        }
    }

    @ExceptionHandler(java.sql.SQLIntegrityConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result dbViolationException(java.sql.SQLIntegrityConstraintViolationException exception, final HttpServletRequest request) {
        insertErrorLog(request, exception, "数据库验证异常，返回BAD_REQUEST");

        if(log.isDebugEnabled()) {
            return Result.errorInternal(exception.getMessage());
        }else {
            return Result.errorInternal("对不起，外键约束失败导致插入或更新数据出错，请稍后再试");
        }
    }

    @ExceptionHandler(org.springframework.dao.DuplicateKeyException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result dbDuplicateKeyException(org.springframework.dao.DuplicateKeyException exception, final HttpServletRequest request) {
        insertErrorLog(request, exception, "数据库冲突异常，返回BAD_REQUEST");

        if(log.isDebugEnabled()) {
            return Result.errorInternal("对不起，发现数据冲突，请更换关键字后重试："+exception.getMessage());
        }else {
            return Result.errorInternal("对不起，发现数据冲突，请更换关键字后重试");
        }
    }

    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result methodArgumentNotValidException(org.springframework.web.bind.MethodArgumentNotValidException exception, final HttpServletRequest request) {
        insertErrorLog(request, exception, "方法参数验证异常，返回BAD_REQUEST");

        return Result.errorBindErrors(exception.getBindingResult().getFieldErrors());
    }

    @ExceptionHandler(PersistenceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result persistenceException(PersistenceException exception, final HttpServletRequest request) {
        insertErrorLog(request, exception, "数据库存储异常，返回INTERNAL_SERVER_ERROR");

        if(log.isDebugEnabled()) {
            return Result.errorInternal(exception.getMessage());
        }else {
            return Result.errorInternal("对不起，服务器(数据访问)出错，请稍后再试");
        }
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result authenticationException(AuthenticationException exception, final HttpServletRequest request) {
        insertErrorLog(request, exception, "授权验证异常，返回UNAUTHORIZED");
        return Result.errorResult(401, exception.getMessage());
    }

    @ExceptionHandler({AccountStatusException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Result authenticationExceptionCredentialsExpired(AccountStatusException exception, final HttpServletRequest request) {
        int errorCode = 4031;
        if (exception instanceof LockedException) {
            errorCode = 4031;
        }else if (exception instanceof CredentialsExpiredException) {
            errorCode = 4032;
        }else if (exception instanceof AccountExpiredException) {
            errorCode = 4033;
        }else if (exception instanceof DisabledException) {
            errorCode = 4034;
        }

        insertErrorLog(request, exception, String.format("账户状态异常(%s)，返回码:%s", errorCode, "FORBIDDEN"));
        return Result.errorResult(errorCode, exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result otherException(Exception exception, final HttpServletRequest request) {
        insertErrorLog(request, exception, "未知异常，返回INTERNAL_SERVER_ERROR");

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
                jdbcTemplate.saveError(args);
            }catch (Exception e) {
                log.error(String.format("打印报错日志：%s 发生错误：%s", ex, e));
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
            args[6] = requestUri;
            args[7] = StringUtils.getMapToParams(params);
        }
    }
}