package cn.watsontech.webhelper.mybatis.param;

import io.swagger.annotations.ApiModelProperty;

/**
 * 带搜索条件的分页参数
 * Created by Watson on 2021/02/28.
 */
public interface TimeRangePageParams {

    @ApiModelProperty(value = "分页码", example="0")
    //从0开始，默认为空，若传该参数则使用该值，若未传则使用p，默认offset为0，p为1
    Integer getOffset();

    @ApiModelProperty(value = "页面大小，默认20", example="20")
    //默认为空，若传该参数则使用该值，若未传则使用ps，默认size为20
    Integer getLimit();

    @ApiModelProperty(value = "排序sortBy", example="createdTime")
    String getSby();

    @ApiModelProperty(value = "升降序", example="desc")
    PageParams.Order getOrd();

    String getFromDate();
    String getToDate();
    String getFromTime();
    String getToTime();

}
