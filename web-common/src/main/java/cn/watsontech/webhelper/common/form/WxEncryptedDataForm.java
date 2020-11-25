package cn.watsontech.webhelper.common.form;

import javax.validation.constraints.NotEmpty;

/**
 * Created by watson on 2020/02/19.
 */
public class WxEncryptedDataForm {
    @NotEmpty(message = "加密内容不能为空")
    String encryptedData;
    @NotEmpty(message = "加密内容IV不能为空")
    String iv;
    String rawData;
    String signature;

    public String getEncryptedData() {
        return encryptedData;
    }

    public void setEncryptedData(String encryptedData) {
        this.encryptedData = encryptedData;
    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }

    public String getRawData() {
        return rawData;
    }

    public void setRawData(String rawData) {
        this.rawData = rawData;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
