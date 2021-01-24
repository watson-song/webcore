package cn.watsontech.webhelper.openapi.form.wx;

import cn.watsontech.webhelper.openapi.params.base.OpenApiParams;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * Created by Watson on 2020/2/22.
 */
public class WxMaSubscribeMsgForm extends WxMsgForm implements OpenApiParams {

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
     * 参数：page
     * 是否必填： 否
     * 描述： 点击模板卡片后的跳转页面，仅限本小程序内的页面。支持带参数,（示例index?foo=bar）。该字段不填则模板无跳转。
     * </pre>
     */
    private String page;

    public WxMaSubscribeMsgForm(){
        super();
    }
    public WxMaSubscribeMsgForm(String wxAppid, String templateId, String toUser, String page, List<WxMessageData> data) {
        super(data);
        this.wxAppid = wxAppid;
        this.toUser = toUser;
        this.templateId = templateId;
        this.page = page;
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

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }
}
