/*
 * Copyright (c) 2020. 串普网络科技有限公司版权所有.
 */

package cn.watsontech.core.web.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Pattern;

/**
 * Created by Watson on 2019/11/7.
 */
@Data
public class ChangePasswordForm {

    @Length(min = 6, max = 20, message = "密码长度最小6位，最大20位")
//    @Pattern(regexp = "[0-9]{1,}[a-zA-Z]{1,}[-_!?./\\|+*~@#%^&;:',.=()\\[\\]\\{\\}]{0,}", message = "必须包含至少一个数字、字母或符号[-_!?./\\|+*~@#%^&;:',.=(){}]")
//    @Pattern(regexp = "[0-9]{1,}[a-zA-Z]{1,}[-_!+*@]{0,}", message = "密码必须包含至少一个数字和字母")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d\\-_!+*@]{6,20}$", message = "密码必须包含至少一个数字和字母")
    @ApiModelProperty(notes = "密码6-20位，数字和字母组合")
    String password;
}