package cn.watsontech.webhelper.common.result;

/**
 * Created by Watson on 2019/12/20.
 */
public class ResultError {
    String field;
    String message;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
