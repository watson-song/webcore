package cn.watsontech.core.web.spring.security.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@ApiModel(value="cn.watsontech.core.security.entity.Role")
@Table(name = "tb_role")
public class Role {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value="id")
    private Long id;

    @ApiModelProperty(value="description")
    private String description;

    @ApiModelProperty(value="name")
    private String name;

    @ApiModelProperty(value="tag")
    private String tag;

    /**
     * 0禁用，1可以
     */
    @ApiModelProperty(value="status0禁用，1可以")
    private Integer status;

    /**
     * 0系统自带，1用户创建的
     */
    @ApiModelProperty(value="type0系统自带，1用户创建的")
    private Integer type;

    @Column(name = "created_by")
    @ApiModelProperty(value="createdBy")
    private String createdBy;

    /**
     * 创建时间
     */
    @Column(name = "created_time")
    @ApiModelProperty(value="createdTime创建时间")
    private Date createdTime;

    /**
     * 版本号
     */
    @ApiModelProperty(value="version版本号")
    private Integer version;

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public Role setId(Long id) {
        this.id = id;
        return this;
    }

    /**
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     */
    public Role setDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public Role setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * @return tag
     */
    public String getTag() {
        return tag;
    }

    /**
     * @param tag
     */
    public Role setTag(String tag) {
        this.tag = tag;
        return this;
    }

    /**
     * 获取0禁用，1可以
     *
     * @return status - 0禁用，1可以
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置0禁用，1可以
     *
     * @param status 0禁用，1可以
     */
    public Role setStatus(Integer status) {
        this.status = status;
        return this;
    }

    /**
     * 获取0系统自带，1用户创建的
     *
     * @return type - 0系统自带，1用户创建的
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置0系统自带，1用户创建的
     *
     * @param type 0系统自带，1用户创建的
     */
    public Role setType(Integer type) {
        this.type = type;
        return this;
    }

    /**
     * @return created_by
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy
     */
    public Role setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    /**
     * 获取创建时间
     *
     * @return created_time - 创建时间
     */
    public Date getCreatedTime() {
        return createdTime;
    }

    /**
     * 设置创建时间
     *
     * @param createdTime 创建时间
     */
    public Role setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
        return this;
    }

    /**
     * 获取版本号
     *
     * @return version - 版本号
     */
    public Integer getVersion() {
        return version;
    }

    /**
     * 设置版本号
     *
     * @param version 版本号
     */
    public Role setVersion(Integer version) {
        this.version = version;
        return this;
    }
}