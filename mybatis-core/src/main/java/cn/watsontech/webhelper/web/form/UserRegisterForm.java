package cn.watsontech.webhelper.web.form;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class UserRegisterForm {

    /**
     * 注册手机号码
     */
    @ApiModelProperty(notes = "手机号码")
    @NotBlank(message = "手机号码不能为空")
    private String mobile;

    /**
     * 客户姓名
     */
    @ApiModelProperty(value = "短信验证码", required = true)
    @NotBlank(message = "短信验证码不能为空")
    private String code;

    /**
     * 密码
     */
    @Length(min = 6, max = 20, message = "密码长度最小6位，最大20位")
//    @Pattern(regexp = "[0-9]{1,}[a-zA-Z]{1,}[-_!+*@]{0,}", message = "必须包含至少一个数字和字母")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d\\-_!+*@]{6,20}$", message = "密码必须包含至少一个数字和字母")
    @ApiModelProperty(value = "密码6-20位，数字和字母组合")
    private String password;

    /**
     * 设备信息
     */
    @ApiModelProperty(notes = "设备信息，具体内容: {\n" +
            "model: brand + \" \" + model,  //手机型号：品牌 + \" \" + 手机型号,\n" +
            "os: Android/IOS + version,\n" +
            "resolution: screenWidth + \"*\" + screenHeight,\n" +
            "deviceNo: 唯一设备号,\n" +
            "netOperator: 运营商\n" +
            "}，注意若无权限则可以放弃获取相关内容", required = true)
    @NotNull(message = "设备信息不能为空")
    private JSONObject deviceTags;

}