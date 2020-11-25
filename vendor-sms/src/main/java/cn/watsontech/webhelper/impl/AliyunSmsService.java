package cn.watsontech.webhelper.impl;

import cn.watsontech.webhelper.SmsProperties;
import cn.watsontech.webhelper.SmsService;
import cn.watsontech.webhelper.utils.MapBuilder;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 使用阿里云短信服务需要添加以下依赖
 *
 * <dependency>
 *    <groupId>com.aliyun</groupId>
 *    <artifactId>aliyun-java-sdk-webhelper</artifactId>
 *    <version>4.0.3</version>
 * </dependency>
 *
 * Copyright to watsontech
 * Created by watson on 2019/12/22.
 */
@ConditionalOnClass(IAcsClient.class)
public class AliyunSmsService extends SmsService {
    Logger log = Logger.getLogger(getClass().getName());

    class AliyunSmsSender implements SmsService.SmsSender {

        //缓存已经初始化的aliyun SMS发送端实例
        final IAcsClient aliyunClient;

        public AliyunSmsSender(IAcsClient aliyunClient) {
            this.aliyunClient = aliyunClient;
        }

        public IAcsClient getAliyunClient() {
            return aliyunClient;
        }
    }

    public AliyunSmsService(SmsProperties smsProperties) {
        super(smsProperties);
    }

    /**
     * 发送短信通知
     * @param mobile 电话号码
     *【短信签名】Hi，你购买的xxxxxxxxxxxxx，订单号：613371265739494244 ，场次时间为：2019.10.25 19:00，为保证观演体验，请提前到达演出场馆现场取票，你可以通过小程序随时查看订单详情。
     */
    private Map sendAliyunRequest(AliyunSmsSender smsSender, String signName, String templateId, String mobile, String templateParams, String[] templateDatas) {
        IAcsClient aliyunClient = smsSender.getAliyunClient();

        String errMsg = "未知错误";
        CommonRequest request = prepareAliyunSendRequest(templateId, signName, mobile, templateParams, templateDatas);
        try {
            CommonResponse response = aliyunClient.getCommonResponse(request);
            log.info("短信发送接口调用成功："+response.getData());

            String responseCode = "Error";
            String responseMessage = "";
            Map responseDataMap = null;
            if (!StringUtils.isEmpty(response.getData())) {
                responseDataMap = new ObjectMapper().createParser(response.getData()).readValueAs(Map.class);
                if (responseDataMap!=null) {
                    responseCode = (String)responseDataMap.get("Code");
                    responseMessage = (String)responseDataMap.get("Message");
                }
            }

            if("OK".equalsIgnoreCase(responseCode)) {
                return responseDataMap;
            }

            errMsg = responseMessage;
        } catch (ServerException e) {
            errMsg = e.getMessage();
            log.log(Level.WARNING, String.format("发送短信失败：%s", errMsg));
        } catch (ClientException e) {
            errMsg = e.getMessage();
            log.log(Level.WARNING, String.format("发送短信失败：%s", errMsg));
        } catch (IOException e) {
            errMsg = e.getMessage();
            log.log(Level.WARNING, String.format("发送短信失败：%s", errMsg));
        }

        log.log(Level.WARNING, String.format("发送短信(%s)失败, 短信参数：%s", mobile, StringUtils.arrayToCommaDelimitedString(templateDatas)));
        throw new IllegalArgumentException("发送短信失败："+errMsg);
    }

    private CommonRequest prepareAliyunSendRequest(String templateId, String signName, String mobile, String templateParams, String[] templateDatas) {
        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");

        request.putQueryParameter("TemplateCode", templateId);
        request.putQueryParameter("SignName", signName);
        request.putQueryParameter("PhoneNumbers", mobile);
        request.putQueryParameter("TemplateParam", String.format(templateParams, templateDatas));
        return request;
    }

    @Override
    protected synchronized SmsSender initSmsSender(SmsProperties smsProperties) {
        //加载配置信息
        String smsAppid = smsProperties.getAppKey(), smsAppkey = smsProperties.getAppSecret(), regionId = smsProperties.getRegionId();
        Assert.notNull(smsAppid, "阿里云短信服务appkey配置信息(sms.appkey)不能为空");
        Assert.notNull(smsAppkey, "阿里云短信服务appSecret配置信息(sms.appSecret)不能为空");
        Assert.notNull(regionId, "阿里云短信服务regionId配置信息(sms.regionId)不能为空");

        DefaultProfile profile = DefaultProfile.getProfile(regionId, smsAppid, smsAppkey);
        IAcsClient aliyunClient = new DefaultAcsClient(profile);

        return new AliyunSmsSender(aliyunClient);
    }

    @Override
    protected SmsSendResult fireSmsSendRequest(SmsSender sender, String smsSign, String templateId, String mobile, String templateParams, String[] templateDatas, String ext) {
        Assert.notNull(templateParams, "阿里云短信服务需提供短信参数，请在smsProperties里设置短信参数");

        Map detail = sendAliyunRequest((AliyunSmsSender) sender, smsSign, templateId, mobile, templateParams, templateDatas);

        Map result = MapBuilder.builder().putNext("mobile", mobile).putNext("errMsg", detail.getOrDefault("Message", ""));
        result.putAll(detail);

        return new SmsSendResult(1, result);
    }

    @Override
    protected List<SmsSendResult> fireSmsSendRequest(SmsSender sender, String smsSign, String templateId, String[] mobiles, String templateParams, String[] templateDatas, String ext) {
        List<SmsSendResult> results = new ArrayList<>(mobiles.length);
        for (int i = 0; i < mobiles.length; i++) {
            results.add(fireSmsSendRequest(sender, smsSign, templateId, mobiles[i], templateParams, templateDatas, ext));
        }
        return results;
    }
}