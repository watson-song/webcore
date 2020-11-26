package cn.watsontech.webhelper.common.form;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

/**
 * Created by Watson on 2019/10/09.
 */
public class UserLoginForm {

    @NotBlank(message = "用户名不能为空")
    @ApiModelProperty(name = "用户名", notes = "用户名@用户类型， 例如：admin@admin, @worker, @user", example = "sow@admin")
    String username;

    @NotBlank(message = "密码不能为空")
//    @Length(min = 6, max = 20, message = "密码长度最小6位，最大20位")
//    @Pattern(regexp = "[0-9]{1,}[a-zA-Z]{1,}[-_!?./\\|+*~@#%^&;:',.=()\\[\\]\\{\\}]{0,}", message = "必须包含至少一个数组、字母和符号[-_!?./\\|+*~@#%^&;:',.=(){}]")
    @ApiModelProperty(name = "密码", notes = "密码", example = "xxxxxx")
    String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}