package cn.watsontech.webhelper.utils.result;

/**
 * Created by Watson on 2019/12/20.
 */
@FunctionalInterface
public interface ResultError {

    String[] getCodes();

    default Object[] getArguments() {
        return null;
    }

    default String getCode() {
        String[] codes = getCodes();
        return codes != null && codes.length > 0?codes[codes.length - 1]:null;
    }
    default String getField() {
        return null;
    }
    default Object getRejectedValue() {
        return null;
    }
    default String getDefaultMessage() {
        return null;
    }
}
