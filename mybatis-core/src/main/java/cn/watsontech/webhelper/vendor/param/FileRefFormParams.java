package cn.watsontech.webhelper.vendor.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Copyright to watsontech
 * Created by Watson on 2020/2/15.
 */
@Data
public class FileRefFormParams {
    //引用实体id
    @NotNull(message = "引用实体id不能为空")
    @ApiModelProperty(notes = "引用物料id")
    String refObjectId;

    //引用实体类型
    @NotNull(message = "引用实体类型不能为空")
    @ApiModelProperty(notes = "引用物料类型，值随便填，orderImage，kaigongImage，wangongImage都可以，有意义的就行")
    String refObjectType;

    @ApiModelProperty(notes = "是否是图片，若未true，则生成的url可能根据配置不同会有单独的图片域名")
    boolean isImage = false;
    @ApiModelProperty(notes = "存储类型：0 归档存储，1 低频存储，2 标准存储")
    int storageLeve = 2;//*0 归档存储，1 低频存储，2 标准存储*/

    //是否需要安全检查（需配置腾讯小程序appid和secret）
    @ApiModelProperty(notes = "是否需要安全检查（需配置腾讯小程序appid和secret）")
    boolean needSecCheck;//securityCheck
}