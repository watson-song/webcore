/*** @generatedBy Watson WebCore 2020-11-30 11:44:53*/
package cn.watsontech.web.template.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.Date;

@ApiModel(value="Article")
@Table(name = "tb_article")
public class Article implements cn.watsontech.webhelper.utils.mybatis.CreatedEntity<Article, Long, Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value="id")
    private Long id;

    @ApiModelProperty(value="title")
    private String title;

    @Column(name = "created_time")
    @ApiModelProperty(value="createdTime")
    private Date createdTime;

    @Column(name = "created_by")
    @ApiModelProperty(value="createdBy")
    private Long createdBy;

    @Column(name = "created_by_name")
    @ApiModelProperty(value="createdByName")
    private String createdByName;

    @Column(name = "modified_time")
    @ApiModelProperty(value="modifiedTime")
    private Date modifiedTime;

    @Column(name = "modified_by")
    @ApiModelProperty(value="modifiedBy")
    private Long modifiedBy;

    @ApiModelProperty(value="version")
    private Integer version;

    @ApiModelProperty(value="content")
    private String content;

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public Article setId(Long id) {
        this.id = id;
        return this;
    }

    /**
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title
     */
    public Article setTitle(String title) {
        this.title = title;
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
    public Article setCreatedTime(Date createdTime) {
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
    public Article setCreatedBy(Long createdBy) {
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
    public Article setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
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
    public Article setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
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
    public Article setModifiedBy(Long modifiedBy) {
        this.modifiedBy = modifiedBy;
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
    public Article setVersion(Integer version) {
        this.version = version;
        return this;
    }

    /**
     * @return content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content
     */
    public Article setContent(String content) {
        this.content = content;
        return this;
    }
}