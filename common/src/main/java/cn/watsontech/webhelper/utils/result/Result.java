package cn.watsontech.webhelper.utils.result;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Watson on 2019/12/18.
 */
public interface Result<T> {

//    @ApiModelProperty(notes = "返回码，200（成功）")
//    int code = 200;
//    @ApiModelProperty(notes = "成功返回数据")
//    T data;
//
//    @ApiModelProperty(notes = "错误内容/成功消息")
//    Object message;
//
//    /**
//     * @deprecated error字段即将废弃，使用message代替 @see message
//     */
//    @ApiModelProperty(notes = "错误内容/成功消息")
//    Object error;
//
//    @ApiModelProperty(notes = "仅当有多条错误提示时有此值")
//    List errors;
//
//    Result() {}
//
//    Result(int code, T data, Object message) {
//        this.code = code;
//        this.data = data;
//        this.message = message;
//        this.error = message;
//    }
//
//    Result(int code, T data) {
//        this.code = code;
//        this.data = data;
//    }
//
//    Result(int code, String message, List errors) {
//        this.code = code;
//        this.message = message;
//        this.error = message;
//        this.errors = errors;
//    }
//
//    Result(int code) {
//        this.code = code;
//    }

//    static <T> Result listResult(List<T> data, Integer page, Integer pageSize, Long total) {
//        return new Result(ResultCode.SUCCESS, new ResultList(data, Long.valueOf(page), pageSize, total));
//    }
//
//    static <T> Result listResult(List<T> data) {
//        return new Result(ResultCode.SUCCESS, new ResultList(data, 0l, data.size(), new Long(data.size())));
//    }
//
//    /**
//     * 唯一的抽象方法，需要实现pageResult方法
//     * if (data instanceof Page) {
//     *      return new Result(ResultCode.SUCCESS, new ResultList(((Page) data).getResult(), ((Page) data).getStartRow(), ((Page) data).getPageSize(), ((Page) data).getTotal()));
//     * }
//     *
//     * return listResult(data);
//     */
//    static <T> Result pageResult(ResultPage<T> data) {
//        return new Result(ResultCode.SUCCESS, new ResultList(data.getResult(), data.getStartRow(), data.getPageSize(), data.getTotal()));
//    }
//
//    static Result errorBadRequest(String error) {
//        return new Result(ResultCode.BAD_REQUEST, null, error);
//    }
//
//    static Result errorNotFound(String error) {
//        return new Result(ResultCode.NOT_FOUND, null, error);
//    }
//
//    /**
//     * bingError包装方法
//     * @param fieldErrors 绑定错误列表
//     *
//     * return new Result(ResultCode.BAD_REQUEST, fieldErrors.get(0).getDefaultMessage(), fieldErrors.stream().sorted((v1, v2) -> v1.getCode().compareTo(v2.getCode())).map(fieldError -> wrapFieldError(fieldError)).collect(Collectors.toList()));
//     */
//    static Result errorBindErrors(List<ResultError> fieldErrors) {
//        return new Result(ResultCode.BAD_REQUEST, fieldErrors.get(0).getDefaultMessage(), fieldErrors.stream().sorted((v1, v2) -> v1.getCode().compareTo(v2.getCode())).map(fieldError -> wrapFieldError(fieldError)).collect(Collectors.toList()));
//    }
//
//    static Result errorInternal(String error) {
//        return new Result(ResultCode.INTERNAL_ERROR, null, error);
//    }
//
//    static Result errorResult(int errorCode, String error) {
//        return new Result(errorCode, null, error);
//    }
//
//    static Result successResult(Object data) {
//        return new Result(ResultCode.SUCCESS, data);
//    }
//
//    static Result successBaseResult(Object data) {
//        return new Result(ResultCode.SUCCESS, wrapMap("success", data));
//    }
//
//    static Result successCreateResult(Long id) {
//        return new Result(ResultCode.SUCCESS, wrapMap("id", id));
//    }

    /**
     * 参数绑定错误包装
     * @param fieldError org.springframework.validation.FieldError类型
     * @return {field, error}
     *
     * Map<String, Object> fieldErrorMap = new HashMap();
     * fieldErrorMap.put("field", fieldError.getField());
     * fieldErrorMap.put("error", fieldError.getDefaultMessage());
     * return fieldErrorMap;
     */
    static Map<String, Object> wrapFieldError(ResultError fieldError) {
        Map<String, Object> fieldErrorMap = new HashMap();
        fieldErrorMap.put("field", fieldError.getField());
        fieldErrorMap.put("error", fieldError.getDefaultMessage());
        return fieldErrorMap;
    }

    static Map<String, Object> wrapMap(String key, Object value) {
        Map<String, Object> idMap = new HashMap<>();
        idMap.put(key, value);
        return idMap;
    }

    int getCode();

    void setCode(int code);

    T getData();

    void setData(T data);

    Object getMessage();

    void setMessage(Object message);

    Object getError();

    void setError(Object error);

    List getErrors();

    void setErrors(List errors);
}
