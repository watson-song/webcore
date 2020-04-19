package cn.watsontech.core.web.spring.security.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import javax.persistence.*;

@ApiModel(value="cn.watsontech.core.security.entity.Permission")
@Table(name = "tb_permission")
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value="id")
    private Long id;

    @ApiModelProperty(value="description")
    private String description;

    @ApiModelProperty(value="name")
    private String name;

    @Column(name = "parent_id")
    @ApiModelProperty(value="parentId")
    private Long parentId;

    @ApiModelProperty(value="weight")
    private Integer weight;

    @ApiModelProperty(value="version")
    @tk.mybatis.mapper.annotation.Version
    private Integer version;

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
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     */
    public Permission setDescription(String description) {
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

    /**
     * @return created_by
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy
     */
    public Permission setCreatedBy(String createdBy) {
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