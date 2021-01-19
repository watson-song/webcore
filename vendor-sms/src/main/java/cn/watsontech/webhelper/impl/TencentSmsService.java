package cn.watsontech.webhelper.impl;

import cn.watsontech.webhelper.SmsProperties;
import cn.watsontech.webhelper.SmsService;
import cn.watsontech.webhelper.utils.MapBuilder;
import com.github.qcloudsms.SmsMultiSender;
import com.github.qcloudsms.SmsMultiSenderResult;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 使用腾讯云短信服务需要添加以下依赖
 *
 * <dependency>
 *   <groupId>com.github.qcloudsms</groupId>
 *   <artifactId>qcloudsms</artifactId>
 *   <version>1.0.6</version>
 * </dependency>
 *
 * Copyright to watsontech
 * Created by watson on 2019/12/22.
 */
@ConditionalOnClass(SmsSingleSender.class)
public class TencentSmsService extends SmsService {
    protected static final Log log = LogFactory.getLog(TencentSmsService.class);

    class TencentSmsSender implements SmsSender {

        //缓存已经初始化的tencent SMS发送端实例
        final SmsSingleSender singleSender;
        final SmsMultiSender multiSender;

        public TencentSmsSender(SmsSingleSender singleSender, SmsMultiSender multiSender) {
            this.singleSender = singleSender;
            this.multiSender = multiSender;
        }

        public SmsMultiSender getMultiSender() {
            return multiSender;
        }

        public SmsSingleSender getSingleSender() {
            return singleSender;
        }
    }

    public TencentSmsService(SmsProperties smsProperties) {
        super(smsProperties);
    }

    /**
     * 发送短信通知
     * @param mobile 电话号码
     * @param ext  用户id
     *【短信签名】Hi，你购买的xxxxxxxxxxxxx，订单号：613371265739494244 ，场次时间为：2019.10.25 19:00，为保证观演体验，请提前到达演出场馆现场取票，你可以通过小程序随时查看订单详情。
     */
    private SmsSingleSenderResult sendTencentRequest(TencentSmsSender smsSender, String smsSign, String templateId, String mobile, String[] templateDatas, String ext) {
        Assert.notNull(mobile, "短信号码不能为空");

        String errMsg = "未知错误";
        try {
            SmsSingleSenderResult result = smsSender.getSingleSender().sendWithParam("86", mobile, Integer.parseInt(templateId), templateDatas, smsSign, "", ext);  // 签名参数未提供或者为空时，会使用默认签名发送短信
            if(result.result==0) {
                //请求成功
                log.info(String.format("发送短信(%s)成功, 短信参数：%s", mobile, StringUtils.arrayToCommaDelimitedString(templateDatas)));
                return result;
            }

            errMsg = result.errMsg;
        } catch (HTTPException e) {
            // HTTP响应码错误
            errMsg = e.getMessage();
            log.error("发送短信失败", e);
        } catch (IOException e) {
            // 网络IO错误
            errMsg = e.getMessage();
            log.error("发送短信失败", e);
        }

        log.error(String.format("发送短信(%s)失败, 短信参数：%s", mobile, StringUtils.arrayToCommaDelimitedString(templateDatas)));
        throw new IllegalArgumentException("发送短信失败："+errMsg);
    }

    /**
     * 发送短信通知
     * @param mobiles 电话号码
     * @param ext  用户id
     *【短信签名】Hi，你购买的xxxxxxxxxxxxx，订单号：613371265739494244 ，场次时间为：2019.10.25 19:00，为保证观演体验，请提前到达演出场馆现场取票，你可以通过小程序随时查看订单详情。
     */
    private List<SmsMultiSenderResult.Detail> sendTencentMultipleRequest(TencentSmsSender smsSender, String smsSign, String templateId, String[] mobiles, String[] templateDatas, String ext) {
        Assert.notNull(mobiles, "短信号码不能为空");

        try {
            SmsMultiSenderResult result = smsSender.getMultiSender().sendWithParam("86", mobiles, Integer.parseInt(templateId), templateDatas, smsSign, "", ext);  // 签名参数未提供或者为空时，会使用默认签名发送短信
            if(result.result==0) {
                //请求成功
                log.info(String.format("发送短信(%s)成功, 短信参数：%s", StringUtils.arrayToCommaDelimitedString(mobiles), StringUtils.arrayToCommaDelimitedString(templateDatas)));
                return result.details;
            }

            log.error(String.format("发送短信(%s)失败, 短信参数：%s", StringUtils.arrayToCommaDelimitedString(mobiles), StringUtils.arrayToCommaDelimitedString(templateDatas)));
            throw new IllegalArgumentException("发送短信失败："+result.errMsg);
        } catch (HTTPException e) {
            // HTTP响应码错误
            e.printStackTrace();
            log.error("发送短信失败", e);
        } catch (IOException e) {
            // 网络IO错误
            e.printStackTrace();
            log.error("发送短信失败", e);
        }

        return Collections.emptyList();
    }

    /**
     * 初始化tencent sender
     */
    private SmsSingleSender initTencentSingleSender(Integer smsAppid, String smsAppKey) {
        return new SmsSingleSender(smsAppid, smsAppKey);
    }

    /**
     * 初始化tencent sender
     */
    private SmsMultiSender initTencentMultiSender(Integer smsAppid, String smsAppKey) {
        return new SmsMultiSender(smsAppid, smsAppKey);
    }

    @Override
    protected synchronized SmsSender initSmsSender(SmsProperties smsProperties) {
        //加载配置信息
        String smsAppid = smsProperties.getAppKey(), smsAppkey = smsProperties.getAppSecret();
        Assert.notNull(smsAppid, "腾讯云短信服务appkey配置信息(sms.appkey)不能为空");
        Assert.notNull(smsAppkey, "腾讯云短信服务appSecret配置信息(sms.appSecret)不能为空");

        Integer tencentAppId = Integer.parseInt(smsAppid);

        SmsSingleSender singleSender =  initTencentSingleSender(tencentAppId, smsAppkey);
        SmsMultiSender multiSender = initTencentMultiSender(tencentAppId, smsAppkey);

        return new TencentSmsSender(singleSender, multiSender);
    }

    @Override
    protected SmsSendResult fireSmsSendRequest(SmsSender sender, String smsSign, String templateId, String mobile, String templateParams, String[] templateDatas, String ext) {
        SmsSingleSenderResult detail = sendTencentRequest((TencentSmsSender) sender, smsSign, templateId, mobile, templateDatas, ext);
        return new SmsSendResult(detail.result, MapBuilder.builder().putNext("sid", detail.sid).putNext("fee", detail.fee).putNext("mobile", mobile).putNext("errMsg", detail.errMsg));
    }

    @Override
    protected List<SmsSendResult> fireSmsSendRequest(SmsSender sender, String smsSign, String templateId, String[] mobiles, String templateParams, String[] templateDatas, String ext) {
        List<SmsMultiSenderResult.Detail> details = sendTencentMultipleRequest((TencentSmsSender) sender, smsSign, templateId, mobiles, templateDatas, ext);
        List result = details.stream().map(detail -> {
            Map<String, Object> detailMap = MapBuilder.builder().putNext("sid", detail.sid).putNext("fee", detail.fee).putNext("mobile", detail.mobile).putNext("errMsg", detail.errmsg);
            return new SmsSendResult(detail.result, detailMap);
        }).collect(Collectors.toList());
        return result;
    }
}