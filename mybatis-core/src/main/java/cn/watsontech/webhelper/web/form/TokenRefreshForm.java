package cn.watsontech.webhelper.web.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Created by Watson on 2020/03/05.
 */
@Data
@ApiModel("令牌刷新提交表单")
public class TokenRefreshForm {

    @ApiModelProperty(notes = "刷新令牌")
    @NotBlank(message = "刷新令牌不能为空")
    String refreshToken;
}
