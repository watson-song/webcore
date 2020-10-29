package cn.watsontech.core.openapi.form;

import cn.watsontech.core.openapi.params.base.OpenApiParams;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 示例Form，参与签名的POST body参数需要实现 openApiParams
 * Created by watson on 2020/6/17.
 */
@Data
public class WxappLoginForm implements OpenApiParams {

    @NotBlank(message = "appid不能为空")
    private String wxAppid;

    @NotBlank(message = "code不能为空")
    @ApiModelProperty(name = "登录令牌")
    String code;
}