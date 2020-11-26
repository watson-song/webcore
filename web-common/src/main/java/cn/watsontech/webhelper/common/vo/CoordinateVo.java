package cn.watsontech.webhelper.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by Watson on 2020/3/4.
 */
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

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Long getDistance() {
        return distance;
    }

    public void setDistance(Long distance) {
        this.distance = distance;
    }
}
