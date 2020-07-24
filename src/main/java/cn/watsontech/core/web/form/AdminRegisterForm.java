package cn.watsontech.core.web.form;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

@Data
public class AdminRegisterForm {

    @ApiModelProperty(notes = "用户名")
    @NotBlank(message = "用户名不能为空")
    private String username;

    @ApiModelProperty(notes = "部门")
    private String department;

    @ApiModelProperty(notes = "职位")
    private String title;

    /**
     * 头像
     */
    @ApiModelProperty(value="avatarUrl头像")
    private String avatarUrl;

    /**
     * 注册手机号码
     */
    @ApiModelProperty(notes = "电话号码")
    @NotBlank(message = "电话号码不能为空")
    private String mobile;

    @ApiModelProperty(notes = "管理员类型：管理员(1)/运营(2)/财务(3)/锁匠审核员(4)，默认2", allowableValues = "1,2,3,4")
    private int type = 2;

    /**
     * 密码
     */
    @Length(min = 6, max = 20, message = "密码长度最小6位，最大20位")
//    @Pattern(regexp = "[0-9]{1,}[a-zA-Z]{1,}[-_!+*@]{0,}", message = "必须包含至少一个数字和字母")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d\\-_!+*@]{6,20}$", message = "密码必须包含至少一个数字和字母")
    @ApiModelProperty(notes = "密码6-20位，数字和字母组合")
    private String password;

    /**
     * 昵称
     */
    @ApiModelProperty(value="nickName昵称")
    private String nickName;

    /**
     * 性别，0未知，1男，2女
     */
    @ApiModelProperty(value="gender性别，0未知，1男，2女")
    private String gender;

    /**
     * 邮箱
     */
    @ApiModelProperty(value="email邮箱")
    private String email;

    /**
     * 地址
     */
    @ApiModelProperty(value="address地址")
    private String address;

    /**
     * 角色id列表
     */
    @ApiModelProperty(name = "角色id")
    private List<Long> roleIds;

    @ApiModelProperty(name = "额外数据字段")
    private JSONObject extraData;
}