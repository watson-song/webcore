package cn.watsontech.webhelper.common.entity;

import cn.watsontech.webhelper.utils.mybatis.CreatedEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.Date;

@ApiModel
@Table(name = "tb_permission")
public class Permission implements CreatedEntity<Permission, Long, Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value="id")
    private Long id;

    @ApiModelProperty(value="权限名称")
    private String label;

    @ApiModelProperty(value="系统标识")
    private String name;

    @Column(name = "parent_id")
    @ApiModelProperty(value="父亲ID")
    private Long parentId;

    @ApiModelProperty(value="权重")
    private Integer weight;

    @ApiModelProperty(value="是否启用")
    private Boolean enabled;

    @ApiModelProperty(value="版本号")
//    @tk.mybatis.mapper.annotation.Version
    private Integer version;

    @Column(name = "created_by")
    @ApiModelProperty(value="创建人ID")
    private Long createdBy;

    @Column(name = "created_by_name")
    @ApiModelProperty(value="创建人名称")
    private String createdByName;

    /**
     * 创建时间
     */
    @Column(name = "created_time")
    @ApiModelProperty(value="创建时间")
    private Date createdTime;

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public Permission setId(Long id) {
        this.id = id;
        return this;
    }

    /**
     * @return label
     */
    public String getLabel() {
        return label;
    }

    /**
     * @param label
     */
    public Permission setLabel(String label) {
        this.label = label;
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
    public Permission setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * @return parent_id
     */
    public Long getParentId() {
        return parentId;
    }

    /**
     * @param parentId
     */
    public Permission setParentId(Long parentId) {
        this.parentId = parentId;
        return this;
    }

    /**
     * @return weight
     */
    public Integer getWeight() {
        return weight;
    }

    /**
     * @param weight
     */
    public Permission setWeight(Integer weight) {
        this.weight = weight;
        return this;
    }

    /**
     * @return version
     */
    public Integer getVersion() {
        return version;
    }

    /**
     * @param version
     */
    public Permission setVersion(Integer version) {
        this.version = version;
        return this;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * @return created_by
     */
    public Long getCreatedBy() {
        return createdBy;
    }

    @Override
    public String getCreatedByName() {
        return this.createdByName;
    }

    @Override
    public Permission setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
        return this;
    }

    /**
     * @param createdBy
     */
    @Override
    public Permission setCreatedBy(Long createdBy) {
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
    public Permission setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
        return this;
    }
}