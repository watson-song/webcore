package cn.watsontech.webhelper.common.form;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * Created by Watson on 2019/11/7.
 */
public class ResetPasswordForm {

    /**
     * 账户名
     */
    @ApiModelProperty(value = "账户名")
    @NotBlank(message = "账户名不能为空")
    private String username;

    /**
     * 手机验证码
     */
    @ApiModelProperty(value = "旧密码", required = true)
    @NotBlank(message = "旧密码不能为空")
    private String oldPassword;

    /**
     * 新密码
     */
    @Length(min = 6, max = 20, message = "密码长度最小6位，最大20位")
//    @Pattern(regexp = "[0-9]{1,}[a-zA-Z]{1,}[-_!+*@]{0,}", message = "必须包含至少一个数字和字母")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d\\-_!+*@]{6,20}$", message = "密码必须包含至少一个数字和字母")
    @ApiModelProperty(value = "密码6-20位，数字和字母组合")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
