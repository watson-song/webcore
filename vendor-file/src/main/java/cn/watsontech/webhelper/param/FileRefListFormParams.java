package cn.watsontech.webhelper.param;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * Copyright to watsontech
 * Created by Watson on 2020/2/15.
 */
public class FileRefListFormParams {

    @ApiModelProperty(notes = "form表单数组使用datas[0].xxx、datas[1].xxx表示")
    List<FileRefFormParams> datas;

    public List<FileRefFormParams> getDatas() {
        return datas;
    }

    public void setDatas(List<FileRefFormParams> datas) {
        this.datas = datas;
    }
}
