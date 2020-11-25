package cn.watsontech.webhelper.common.form;

import javax.validation.constraints.NotEmpty;

/**
 * Created by watson on 2020/02/19.
 */
public class WxLoginCodeForm {

    @NotEmpty(message = "code不能为空")
    String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
