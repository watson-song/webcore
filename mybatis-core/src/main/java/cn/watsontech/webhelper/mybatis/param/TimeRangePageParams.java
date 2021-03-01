package cn.watsontech.webhelper.mybatis.param;

/**
 * 带搜索条件的分页参数
 * Created by Watson on 2021/02/28.
 */
public interface TimeRangePageParams extends PageParams {

    String getFromDate();
    String getToDate();
    String getFromTime();
    String getToTime();

}
