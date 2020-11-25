package cn.watsontech.webhelper.mybatis.param;

import io.swagger.annotations.ApiModelProperty;

/**
 * Created by watson on 2020/3/22.
 */
public interface ExportParams {
    enum Type {excelOnly,withImage,withZip}

    @ApiModelProperty(value = "导出excel方式，excelOnly/withImage/withZip，默认excelOnly")
    Type getExportType();

    //获取导出列属性
    @ApiModelProperty(value = "查询实体属性列表，默认全部查询")
    String[] getFields();
}
