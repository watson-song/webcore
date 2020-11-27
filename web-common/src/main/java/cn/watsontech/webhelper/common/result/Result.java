package cn.watsontech.webhelper.common.result;

import com.github.pagehelper.Page;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Watson on 2019/12/18.
 */
public class Result<T> {

    @ApiModelProperty(notes = "返回码，200（成功）")
    int code;
    @ApiModelProperty(notes = "成功返回数据")
    T data;

    @ApiModelProperty(notes = "错误内容/成功消息")
    Object message;

    /**
     * @deprecated error字段即将废弃，使用message代替 @see message
     */
    @ApiModelProperty(notes = "错误内容/成功消息")
    Object error;

    @ApiModelProperty(notes = "仅当有多条错误提示时有此值")
    List errors;

    public Result() {}

    public Result(int code, T data, Object message) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.error = message;
    }

    public Result(int code, T data) {
        this.code = code;
        this.data = data;
    }

    public Result(int code, String message, List errors) {
        this.code = code;
        this.message = message;
        this.error = message;
        this.errors = errors;
    }

    public Result(int code) {
        this.code = code;
    }

    public static <T> Result listResult(List<T> data, Integer page, Integer pageSize, Long total) {
        return new Result(ResultCode.SUCCESS, new ResultList(data, page, pageSize, total));
    }

    public static <T> Result listResult(List<T> data) {
        return new Result(ResultCode.SUCCESS, new ResultList(data, 0, data.size(), new Long(data.size())));
    }

    public static <T> Result pageResult(List<T> data) {
        if (data instanceof Page) {
            return new Result(ResultCode.SUCCESS, new ResultList(((Page) data).getResult(), ((Page) data).getStartRow(), ((Page) data).getPageSize(), ((Page) data).getTotal()));
        }

        return listResult(data);
    }

    public static Result errorBadRequest(String error) {
        return new Result(ResultCode.BAD_REQUEST, null, error);
    }

    public static Result errorNotFound(String error) {
        return new Result(ResultCode.NOT_FOUND, null, error);
    }

    public static Result errorBindErrors(List<FieldError> fieldErrors) {
        return new Result(ResultCode.BAD_REQUEST, fieldErrors.get(0).getDefaultMessage(), fieldErrors.stream().sorted((v1, v2) -> v1.getCode().compareTo(v2.getCode())).map(fieldError -> wrapFieldError(fieldError)).collect(Collectors.toList()));
    }

    public static Result errorInternal(String error) {
        return new Result(ResultCode.INTERNAL_ERROR, null, error);
    }

    public static Result errorResult(int errorCode, String error) {
        return new Result(errorCode, null, error);
    }

    public static Result successResult(Object data) {
        return new Result(ResultCode.SUCCESS, data);
    }

    public static Result successBaseResult(Object data) {
        return new Result(ResultCode.SUCCESS, wrapMap("success", data));
    }

    public static Result successCreateResult(Long id) {
        return new Result(ResultCode.SUCCESS, wrapMap("id", id));
    }

    private static Map<String, Object> wrapFieldError(FieldError fieldError) {
        Map<String, Object> fieldErrorMap = new HashMap();
        fieldErrorMap.put("field", fieldError.getField());
        fieldErrorMap.put("error", fieldError.getDefaultMessage());
        return fieldErrorMap;
    }

    protected static Map<String, Object> wrapMap(String key, Object value) {
        Map<String, Object> idMap = new HashMap<>();
        idMap.put(key, value);
        return idMap;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }

    public List getErrors() {
        return errors;
    }

    public void setErrors(List errors) {
        this.errors = errors;
    }
}
