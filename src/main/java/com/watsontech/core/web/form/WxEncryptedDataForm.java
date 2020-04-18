package com.watsontech.core.web.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * Created by watson on 2020/02/19.
 */
@Data
public class WxEncryptedDataForm {
    @NotEmpty(message = "加密内容不能为空")
    String encryptedData;
    @NotEmpty(message = "加密内容IV不能为空")
    String iv;
    String rawData;
    String signature;
}
