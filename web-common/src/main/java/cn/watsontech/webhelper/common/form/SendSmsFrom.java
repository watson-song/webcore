package cn.watsontech.webhelper.common.form;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Created by Watson on 2020/2/8.
 */
public class SendSmsFrom {

    @NotNull(message = "业务名称不能为空")
    @ApiModelProperty(value = "业务名称：bindBankcard/register/resetPassword/walletPassword/resetWalletPassword")
    String buzName;

    @NotBlank(message = "发送手机不能为空")
    @ApiModelProperty(value = "发送手机号")
    String mobile;

    public String getBuzName() {
        return buzName;
    }

    public void setBuzName(String buzName) {
        this.buzName = buzName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
