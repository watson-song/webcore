package cn.watsontech.core.openapi.params;

import cn.watsontech.core.openapi.params.base.OpenApiListParamsVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 示例Form，参数需要继承 OpenApiParamsVo
 * Created by Watson on 2020/02/09.
 */
@Data
public class FileListOpenApiParamsVo extends OpenApiListParamsVo {

    @ApiModelProperty(value = "用户ID", dataType = "java.lang.Long", example="1,2")
    Long uid/*用户ID*/;

    @ApiModelProperty(value = "开始日期 2020-02-09", notes = "日期notes", example="2020-02-09")
    String from;

    @ApiModelProperty(value = "结束日期 2020-02-10", example="2020-02-09")
    String to;

    @ApiModelProperty(value = "文件类型", example="image")
    String type;

    public Date getFromDate() {
        return getFromDate(from);
    }

    public Date getToDate() {
        return getToDate(to);
    }
}
