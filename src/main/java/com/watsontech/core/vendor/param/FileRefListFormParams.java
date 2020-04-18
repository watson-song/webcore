package com.watsontech.core.vendor.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Copyright to watsontech
 * Created by Watson on 2020/2/15.
 */
@Data
public class FileRefListFormParams {

    @ApiModelProperty(notes = "form表单数组使用datas[0].xxx、datas[1].xxx表示")
    List<FileRefFormParams> datas;
}
