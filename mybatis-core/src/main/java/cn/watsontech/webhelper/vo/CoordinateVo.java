package cn.watsontech.webhelper.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Watson on 2020/3/4.
 */
@Data
@ApiModel("经纬度坐标")
public class CoordinateVo {
    /**
     * 纬度
     */
    @ApiModelProperty(value="lat 纬度")
    private Double lat;

    /**
     * 经度
     */
    @ApiModelProperty(value="lng 经度")
    private Double lng;

    /**
     * 距离，单位米
     */
    @ApiModelProperty(value="距离(单位米)")
    private Long distance;
}
