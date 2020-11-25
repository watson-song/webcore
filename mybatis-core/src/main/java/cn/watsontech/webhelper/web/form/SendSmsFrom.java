package cn.watsontech.webhelper.web.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Created by Watson on 2020/2/8.
 */
@Data
public class SendSmsFrom {

    @NotNull(message = "业务名称不能为空")
    @ApiModelProperty(value = "业务名称：bindBankcard/register/resetPassword/walletPassword/resetWalletPassword")
    String buzName;

    @NotBlank(message = "发送手机不能为空")
    @ApiModelProperty(value = "发送手机号")
    String mobile;
}
