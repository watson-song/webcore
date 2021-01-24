package cn.watsontech.webhelper.openapi.form.wx;

import cn.watsontech.webhelper.openapi.params.base.OpenApiParams;

import javax.validation.constraints.NotBlank;

/**
 * 微信公众号一次性订阅消息
 * Created by Watson on 2020/2/22.
 */
public class WxMpSubscribeMsgForm implements OpenApiParams {
    public static class MiniApp {
        String appid;
        String path;

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }

    @NotBlank(message = "appid不能为空")
    private String wxAppid;

    /**
     * 接收者（用户）的 openid.
     * <pre>
     * 参数：touser
     * 是否必填： 是
     * 描述： 接收者（用户）的 openid
     * </pre>
     */
    @NotBlank(message = "接受人openid不能为空")
    private String toUser;

    /**
     * 所需下发的模板消息的id.
     * <pre>
     * 参数：template_id
     * 是否必填： 是
     * 描述： 所需下发的模板消息的id
     * </pre>
     */
    @NotBlank(message = "消息模板id不能为空")
    private String templateId;

    /**
     * 点击模板卡片后的跳转页面，仅限本小程序内的页面.
     * <pre>
     * 参数：url
     * 是否必填： 否
     * 描述： 点击模板卡片后的跳转页面，仅限本小程序内的页面。支持带参数,（示例index?foo=bar）。该字段不填则模板无跳转。
     * </pre>
     */
    private String url;

    /**
     * 订阅场景值
     */
    private String scene;

    /**
     * 消息标题 (15字以内)
     */
    private String title;

    /**
     * 消息内容文本 （200字以内）
     */
    private String contentValue;

    /**
     * 消息内容文本颜色
     */
    private String contentColor;

    private MiniApp miniProgram;

    public WxMpSubscribeMsgForm(){}
    public WxMpSubscribeMsgForm(String wxAppid, String templateId, String toUser, String url, String scene, String title, String contentValue, String contentColor) {
        this.wxAppid = wxAppid;
        this.toUser = toUser;
        this.templateId = templateId;
        this.url = url;
        this.scene = scene;
        this.title = title;
        this.contentValue = contentValue;
        this.contentColor = contentColor;
    }

    public String getWxAppid() {
        return wxAppid;
    }

    public void setWxAppid(String wxAppid) {
        this.wxAppid = wxAppid;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getScene() {
        return scene;
    }

    public void setScene(String scene) {
        this.scene = scene;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContentValue() {
        return contentValue;
    }

    public void setContentValue(String contentValue) {
        this.contentValue = contentValue;
    }

    public String getContentColor() {
        return contentColor;
    }

    public void setContentColor(String contentColor) {
        this.contentColor = contentColor;
    }

    public MiniApp getMiniProgram() {
        return miniProgram;
    }

    public void setMiniProgram(MiniApp miniProgram) {
        this.miniProgram = miniProgram;
    }
}
