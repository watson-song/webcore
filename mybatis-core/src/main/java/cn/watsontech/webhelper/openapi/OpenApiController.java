package cn.watsontech.webhelper.openapi;

import cn.watsontech.webhelper.openapi.params.OpenApiLoginUser;
import cn.watsontech.webhelper.openapi.params.base.OpenApiParamsVo;
import cn.watsontech.webhelper.openapi.service.OpenApiService;
import cn.watsontech.webhelper.web.result.Result;
import cn.watsontech.webhelper.web.spring.aop.annotation.OpenApi;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

/**
 * 示例Controller，使用OpenApi注解、公开接口参数需要继承 openApiParamsVo 和 标记访问人员 OpenApiLoginUser
 * Created by Watson on 2020/02/09.
 */
@RequestMapping("/api/open/v1/{appid}")
public class OpenApiController {
    @Autowired
    OpenApiService service;

    @ApiOperation(value = "发送邮件")
    @PostMapping("/mail/send")
    @OpenApi("发送邮件")
    public @ResponseBody Result sendEmail(@PathVariable String appid, @Valid @RequestBody Map form, @Valid @ModelAttribute OpenApiParamsVo openApiParamsVo, @Valid @ModelAttribute OpenApiLoginUser user) {
        return Result.successBaseResult(service.sendMail(form));
    }

}
