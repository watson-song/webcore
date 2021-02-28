package cn.watsontech.webhelper.mybatis.param;

import io.swagger.annotations.ApiModelProperty;
//import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import javax.validation.constraints.Pattern;

/**
 * 带日期搜索条件的分页参数
 * Created by Watson on 2020/03/06.
 */
public class TimeRangeCriteriaPageParams extends CriteriaPageParams implements TimeRangePageParams {

    /**
     * 开始时间
     */
    @ApiModelProperty(value="开始日期(日期格式：yyyy-MM-dd)")
    @Pattern(regexp = "^(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)$", message = "日期格式不正确，正确格式为：2020-01-01")
    private String fromDate;

    /**
     * 结束时间
     */
    @ApiModelProperty(value="结束日期(日期格式：yyyy-MM-dd)")
    @Pattern(regexp = "^(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)$", message = "日期格式不正确，正确格式为：2020-01-01")
    private String toDate;

    /**
     * 开始时间
     */
    @ApiModelProperty(value="开始时间(格式：yyyy-MM-dd HH:mm:ss)")
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String fromTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value="结束时间(格式：yyyy-MM-dd HH:mm:ss)")
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String toTime;

    @ApiModelProperty(value="时间搜索字段，默认creatdTime")
    String timeRangeField = "createdTime";

    public String getTimeRangeField() {
        return timeRangeField;
    }

    public void setTimeRangeField(String timeRangeField) {
        this.timeRangeField = timeRangeField;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public String getToTime() {
        return toTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }

    @Override
    public void fillCriteria(Example.Criteria criteria) {
        if(!StringUtils.isEmpty(timeRangeField)) {
            String from = fromDate;
            if (StringUtils.isEmpty(from)) {
                from = fromTime;
            }
            if (!StringUtils.isEmpty(from)) {
                criteria.andGreaterThanOrEqualTo(timeRangeField, from);
            }

            String to = toDate;
            if (StringUtils.isEmpty(to)) {
                to = toTime;
            }else {

                //按日期搜索，结束时间默认为23:59:59
                to += " 23:59:59";
            }
            if (!StringUtils.isEmpty(to)) {
                criteria.andLessThan(timeRangeField, to);
            }
        }
    }
}
