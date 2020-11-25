package cn.watsontech.webhelper.vendor;

import cn.watsontech.webhelper.web.spring.config.SmsProperties;
import lombok.extern.log4j.Log4j2;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Copyright to watsontech
 * Created by watson on 2019/12/22.
 */
@Log4j2
public abstract class SmsService {

    /**
     * 短信发送结果
     */
    public class SmsSendResult {
        int result;
        Map<String, Object> extraDatas;

        public SmsSendResult(int result, Map<String, Object> extraDatas) {
            this.result = result;
            this.extraDatas = extraDatas;
        }

        public int getResult() {
            return result;
        }

        public Map<String, Object> getExtraDatas() {
            return extraDatas;
        }
    }

    public interface SmsTemplateKey {
        String name();

        default String label() {
            return name();
        }
    }

    protected SmsProperties smsProperties;
    volatile SmsSender smsSender;

    /**
     * 短信发送实例
     */
    public interface SmsSender {
    }

    public SmsService(SmsProperties smsProperties) {
        this.smsProperties = smsProperties;
    }

    /**
     * 初始化消息发送对象
     * @param smsProperties 短信配置信息
     */
    protected abstract SmsSender initSmsSender(SmsProperties smsProperties);

    /**
     * 请求短信发送
     * @return true发送成功，false发送失败
     */
    protected abstract SmsSendResult fireSmsSendRequest(SmsSender sender, String smsSign, String templateId, String mobile, String templateParams, String[] templateDatas, String ext);

    /**
     * 批量请求短信发送
     * @return true发送成功，false发送失败
     */
    protected abstract List<SmsSendResult> fireSmsSendRequest(SmsSender sender, String smsSign, String templateId, String[] mobiles, String templateParams, String[] templateDatas, String ext);

    /**
     * 批量发送短信，系统根据配置自动选择短信服务提供商
     * @param mobiles 电话
     * @param templateKey 模板在properties中sm.templates的key
     * @param templateDatas 模板数据
     * @param ext 额外数据 仅腾讯云短信有效
     * @return true发送成功，否则失败
     */
    public List<SmsSendResult> sendSms(String[] mobiles, SmsTemplateKey templateKey, List<String> templateDatas, String ext) {
        Assert.isTrue(mobiles!=null&&mobiles.length>0, "电话号码不能为空");

        String[] templateValues = new String[templateDatas.size()];
        templateDatas.toArray(templateValues);

        SmsProperties.TemplateConfig templateConfig = smsProperties.getTemplates().get(templateKey.name());
        Assert.notNull(templateConfig, "短信模板不正确，请确认模板已配置");

        String templateParams = templateConfig.getParams();
        SmsSender smsSender = selectSmsSender();
        if (mobiles.length<2) {
            return Arrays.asList(fireSmsSendRequest(smsSender, smsProperties.getSignName(), templateConfig.getId(), mobiles[0], templateParams, templateValues, ext));
        }

        return fireSmsSendRequest(smsSender, smsProperties.getSignName(), templateConfig.getId(), mobiles, templateParams, templateValues, ext);
    }

    private SmsSender selectSmsSender() {
        SmsSender smsSender = getCachedSmsSender();
        Assert.notNull(smsSender, "短信发送对象未正确实例化，请检查短信配置信息！");
        return smsSender;
    }

    /**
     * 获取已缓存的短信发送实例
     */
    private SmsSender getCachedSmsSender() {
        if (smsSender==null) {
            //加载并初始化短信配置信息
            smsSender = initSmsSender(smsProperties);
        }

        Assert.notNull(smsSender, "SmsSender实例化失败，请检查配置是否正确！");
        return smsSender;
    }
}
