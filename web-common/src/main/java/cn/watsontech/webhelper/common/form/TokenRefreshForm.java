package cn.watsontech.webhelper.common.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

/**
 * Created by Watson on 2020/03/05.
 */
@ApiModel("令牌刷新提交表单")
public class TokenRefreshForm {

    @ApiModelProperty(notes = "刷新令牌")
    @NotBlank(message = "刷新令牌不能为空")
    String refreshToken;

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
