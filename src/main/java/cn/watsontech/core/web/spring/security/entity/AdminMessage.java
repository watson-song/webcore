package cn.watsontech.core.web.spring.security.entity;

import com.alibaba.fastjson.JSONObject;
import cn.watsontech.core.service.intf.Message;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.Date;

@ApiModel(value="cn.watsontech.core.security.entity.AdminMessage")
@Table(name = "tb_admin_message")
public class AdminMessage implements Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value="id")
    private Long id;

    /**
     * 管理员id
     */
    @Column(name = "user_id")
    @ApiModelProperty(value="userId管理员id")
    private Long userId;

    /**
     * 消息类型
     */
    @ApiModelProperty(value="type消息类型")
    private String type;

    /**
     * 标题
     */
    @ApiModelProperty(value="title标题")
    private String title;

    /**
     * 内容
     */
    @ApiModelProperty(value="content内容")
    private String content;

    /**
     * 状态
     */
    @ApiModelProperty(value="state状态")
    private String state;

    /**
     * 状态名称
     */
    @Column(name = "state_desc")
    @ApiModelProperty(value="stateDesc状态名称")
    private String stateDesc;

    /**
     * 是否启用
     */
    @ApiModelProperty(value="enabled是否启用")
    private Boolean enabled;

    /**
     * 版本号
     */
    @ApiModelProperty(value="version版本号")
    private Integer version;

    /**
     * 创建人ID
     */
    @Column(name = "created_by")
    @ApiModelProperty(value="createdBy创建人ID")
    private Long createdBy;

    /**
     * 创建人名称
     */
    @Column(name = "created_by_name")
    @ApiModelProperty(value="createdByName创建人名称")
    private String createdByName;

    @Column(name = "created_time")
    @ApiModelProperty(value="createdTime")
    private Date createdTime;

    /**
     * 最后更新人ID
     */
    @Column(name = "modified_by")
    @ApiModelProperty(value="modifiedBy最后更新人ID")
    private Long modifiedBy;

    /**
     * 最后更新时间
     */
    @Column(name = "modified_time")
    @ApiModelProperty(value="modifiedTime最后更新时间")
    private Date modifiedTime;

    /**
     * 额外数据
     */
    @Column(name = "extra_data")
    @ApiModelProperty(value="extraData额外数据")
    private JSONObject extraData;

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public AdminMessage setId(Long id) {
        this.id = id;
        return this;
    }

    /**
     * 获取管理员id
     *
     * @return user_id - 管理员id
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * 设置管理员id
     *
     * @param userId 管理员id
     */
    public AdminMessage setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    /**
     * 获取消息类型
     *
     * @return type - 消息类型
     */
    public String getType() {
        return type;
    }

    /**
     * 设置消息类型
     *
     * @param type 消息类型
     */
    public AdminMessage setType(String type) {
        this.type = type;
        return this;
    }

    /**
     * 获取标题
     *
     * @return title - 标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置标题
     *
     * @param title 标题
     */
    public AdminMessage setTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * 获取内容
     *
     * @return content - 内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置内容
     *
     * @param content 内容
     */
    public AdminMessage setContent(String content) {
        this.content = content;
        return this;
    }

    /**
     * 获取状态
     *
     * @return state - 状态
     */
    public String getState() {
        return state;
    }

    /**
     * 设置状态
     *
     * @param state 状态
     */
    public AdminMessage setState(String state) {
        this.state = state;
        return this;
    }

    /**
     * 获取状态名称
     *
     * @return state_desc - 状态名称
     */
    public String getStateDesc() {
        return stateDesc;
    }

    /**
     * 设置状态名称
     *
     * @param stateDesc 状态名称
     */
    public AdminMessage setStateDesc(String stateDesc) {
        this.stateDesc = stateDesc;
        return this;
    }

    /**
     * 获取是否启用
     *
     * @return enabled - 是否启用
     */
    public Boolean getEnabled() {
        return enabled;
    }

    /**
     * 设置是否启用
     *
     * @param enabled 是否启用
     */
    public AdminMessage setEnabled(Boolean enabled) {
        this.enabled = enabled;
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
    public AdminMessage setVersion(Integer version) {
        this.version = version;
        return this;
    }

    /**
     * 获取创建人ID
     *
     * @return created_by - 创建人ID
     */
    public Long getCreatedBy() {
        return createdBy;
    }

    /**
     * 设置创建人ID
     *
     * @param createdBy 创建人ID
     */
    public AdminMessage setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    /**
     * 获取创建人名称
     *
     * @return created_by_name - 创建人名称
     */
    public String getCreatedByName() {
        return createdByName;
    }

    /**
     * 设置创建人名称
     *
     * @param createdByName 创建人名称
     */
    public AdminMessage setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
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
    public AdminMessage setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
        return this;
    }

    /**
     * 获取最后更新人ID
     *
     * @return modified_by - 最后更新人ID
     */
    public Long getModifiedBy() {
        return modifiedBy;
    }

    /**
     * 设置最后更新人ID
     *
     * @param modifiedBy 最后更新人ID
     */
    public AdminMessage setModifiedBy(Long modifiedBy) {
        this.modifiedBy = modifiedBy;
        return this;
    }

    /**
     * 获取最后更新时间
     *
     * @return modified_time - 最后更新时间
     */
    public Date getModifiedTime() {
        return modifiedTime;
    }

    /**
     * 设置最后更新时间
     *
     * @param modifiedTime 最后更新时间
     */
    public AdminMessage setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
        return this;
    }

    /**
     * 获取额外数据
     *
     * @return extra_data - 额外数据
     */
    public JSONObject getExtraData() {
        return extraData;
    }

    /**
     * 设置额外数据
     *
     * @param extraData 额外数据
     */
    public AdminMessage setExtraData(JSONObject extraData) {
        this.extraData = extraData;
        return this;
    }
}