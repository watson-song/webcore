package cn.watsontech.core.web.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * Created by watson on 2020/02/19.
 */
@Data
public class WxLoginCodeForm {

    @NotEmpty(message = "code不能为空")
    String code;
}
