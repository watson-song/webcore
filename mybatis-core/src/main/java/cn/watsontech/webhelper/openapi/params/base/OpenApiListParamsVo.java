package cn.watsontech.webhelper.openapi.params.base;

import cn.watsontech.webhelper.utils.date.DateUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * 注意：不能使用此vo直接做请求参数，缺少具体参数
 * Created by Watson on 2020/02/09.
 */
@Data
public class OpenApiListParamsVo extends OpenApiParamsVo {
    @ApiModelProperty(value = "分页码", example="1")
    @NotNull(message = "offset参数不能为空")
    protected Integer offset;  //offset 默认第一页

    @ApiModelProperty(value = "页面大小，默认10", example="10")
    @NotNull(message = "limit参数不能为空")
    protected Integer limit; //limit 默认每页10条

    @ApiModelProperty(value = "排序", example="createdTime")
    protected String sby;  //sortBy

    @ApiModelProperty(value = "升降序", example="desc/asc")
    protected String ord; //order

    public Date getFromDate(String from) {
        if(from!=null) {
            return getDate(from, false);
        }
        return null;
    }

    public Date getToDate(String to) {
        if(to!=null) {
            return getDate(to, true);
        }
        return null;
    }

    public Date getDate(String date, boolean isTo/*是否为截止日，true则添加23：59：59至日期末尾*/) {
        if(date!=null) {

            if (isTo) {
                /*是toDate则添加23：59：59至日期末尾*/
                date = wrapDateTo2359(date);
            }
            return DateUtils.parseDate(date);
        }
        return null;
    }

    /**
     * 包装日期到当日的23点59分
     */
//    static String datePattern_yyyyMMdd = "^[0-9]{4}[-/][0-9]{2}[-/][0-9]{2}$";
    private String wrapDateTo2359 (String date) {
        String datePattern_yyyyMMdd = "^[0-9]{4}[-/][0-9]{2}[-/][0-9]{2}$";
        if (Pattern.compile(datePattern_yyyyMMdd).matcher(date).find()) {
            return date + " 23:59:59";
        }

        return date;
    }

    @Override
    public String toString() {
        return "offset=" + offset + "|limit=" + limit + "|sby=" + sby + "|ord=" + ord;
    }
}
