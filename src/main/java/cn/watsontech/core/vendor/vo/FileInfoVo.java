package cn.watsontech.core.vendor.vo;

import lombok.Data;

import java.util.Date;

/**
 * Created by Watson on 2020/11/18.
 */
@Data
public class FileInfoVo {

    private Long id;

    /**
     * 应用code
     */
    private String appinfoId;

    /**
     * 拥有人
     */
    private Long userId;

    private String userType;

    /**
     * 云端id,aliyun,tencent,ucloud
     */
    private String cProvider;

    /**
     * 文件名称
     */
    private String name;

    /**
     * 访问地址
     */
    private String url;

    /**
     * 云端路径
     */
    private String path;

    /**
     * 云端bucket
     */
    private String bucket;

    /**
     * 文件类型
     */
    private String type;

    /**
     * 文件大小
     */
    private Long size;

    /**
     * 引用对象id
     */
    private String refObjectId;

    /**
     * 引用对象类型
     */
    private String refObjectType;

    /**
     * 标签
     */
    private String tags;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 创建人名称
     */
    private Long createdBy;

    private String createdByName;

    /**
     * 创建时间
     */
    private Date createdTime;
}
