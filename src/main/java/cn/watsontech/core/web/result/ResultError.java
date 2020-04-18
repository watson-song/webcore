package cn.watsontech.core.web.result;

import lombok.Data;

/**
 * Created by Watson on 2019/12/20.
 */
@Data
public class ResultError {
    String field;
    String message;
}
