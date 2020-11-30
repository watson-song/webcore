/*** @generatedBy Watson WebCore 2020-11-30 11:44:53*/
package cn.watsontech.web.template.entity;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import javax.persistence.*;

@ApiModel(value="Category")
@Table(name = "tb_category")
public class Category implements cn.watsontech.webhelper.utils.mybatis.CreatedEntity<Category, Long, Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value="id")
    private Long id;

    @ApiModelProperty(value="name")
    private String name;

    @ApiModelProperty(value="label")
    private String label;

    @Column(name = "created_time")
    @ApiModelProperty(value="createdTime")
    private Date createdTime;

    @Column(name = "created_by")
    @ApiModelProperty(value="createdBy")
    private Long createdBy;

    @Column(name = "created_by_name")
    @ApiModelProperty(value="createdByName")
    private String createdByName;

    @Column(name = "modified_by")
    @ApiModelProperty(value="modifiedBy")
    private Long modifiedBy;

    @Column(name = "modified_time")
    @ApiModelProperty(value="modifiedTime")
    private Date modifiedTime;

    @ApiModelProperty(value="version")
    private Integer version;

    @Column(name = "extra_data")
    @ApiModelProperty(value="extraData")
    private JSON extraData;

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public Category setId(Long id) {
        this.id = id;
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
    public Category setName(String name) {
        this.name = name;
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
    public Category setLabel(String label) {
        this.label = label;
        return this;
    }

    /**
     * @return created_time
     */
    public Date getCreatedTime() {
        return createdTime;
    }

    /**
     * @param createdTime
     */
    public Category setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
        return this;
    }

    /**
     * @return created_by
     */
    public Long getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy
     */
    public Category setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    /**
     * @return created_by_name
     */
    public String getCreatedByName() {
        return createdByName;
    }

    /**
     * @param createdByName
     */
    public Category setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
        return this;
    }

    /**
     * @return modified_by
     */
    public Long getModifiedBy() {
        return modifiedBy;
    }

    /**
     * @param modifiedBy
     */
    public Category setModifiedBy(Long modifiedBy) {
        this.modifiedBy = modifiedBy;
        return this;
    }

    /**
     * @return modified_time
     */
    public Date getModifiedTime() {
        return modifiedTime;
    }

    /**
     * @param modifiedTime
     */
    public Category setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
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
    public Category setVersion(Integer version) {
        this.version = version;
        return this;
    }

    /**
     * @return extra_data
     */
    public JSON getExtraData() {
        return extraData;
    }

    /**
     * @param extraData
     */
    public Category setExtraData(JSON extraData) {
        this.extraData = extraData;
        return this;
    }
}