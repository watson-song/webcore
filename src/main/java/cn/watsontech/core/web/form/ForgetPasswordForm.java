package cn.watsontech.core.web.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * Created by Watson on 2019/11/7.
 */
@Data
public class ForgetPasswordForm {

    /**
     * 注册手机号码
     */
    @ApiModelProperty(value = "手机号码")
    @NotBlank(message = "手机号码不能为空")
    private String mobile;

    /**
     * 手机验证码
     */
    @ApiModelProperty(value = "短信验证码", required = true)
    @NotBlank(message = "短信验证码不能为空")
    private String code;

    /**
     * 新密码
     */
    @Length(min = 6, max = 20, message = "密码长度最小6位，最大20位")
//    @Pattern(regexp = "[0-9]{1,}[a-zA-Z]{1,}[-_!+*@]{0,}", message = "必须包含至少一个数字和字母")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d\\-_!+*@]{6,20}$", message = "密码必须包含至少一个数字和字母")
    @ApiModelProperty(value = "密码6-20位，数字和字母组合")
    private String password;

}
