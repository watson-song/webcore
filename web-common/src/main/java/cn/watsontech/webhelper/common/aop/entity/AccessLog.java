package cn.watsontech.webhelper.common.aop.entity;

import cn.watsontech.webhelper.utils.mybatis.CreatedEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.Date;

@ApiModel(value="cn.watsontech.webhelper.common.aop.entity.Access")
@Table(name = "tb_access_log")
public class AccessLog implements CreatedEntity<AccessLog, Long, Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value="id")
    private Long id;

    /**
     * 组ID
     */
    @ApiModelProperty(value="分组ID")
    @Column(name = "group_id")
    private String groupId;

    /**
     * 日志级别
     */
    @ApiModelProperty(value="level日志级别")
    private String level;

    /**
     * 日志标题
     */
    @ApiModelProperty(value="title日志标题")
    private String title;

    /**
     * 请求ip地址
     */
    @ApiModelProperty(value="ip请求ip地址")
    private String ip;

    /**
     * 请求方式
     */
    @ApiModelProperty(value="method请求方式")
    private String method;

    /**
     * 版本号
     */
    @ApiModelProperty(value="version版本号")
//    @tk.mybatis.mapper.annotation.Version
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

    @Column(name = "created_time")
    @ApiModelProperty(value="createdTime")
    private Date createdTime;

    /**
     * 用户openid
     */
    @ApiModelProperty(value="url用户openid")
    private String url;

    /**
     * 提交参数
     */
    @ApiModelProperty(value="params提交参数")
    private String params;

    /**
     * 异常
     */
    @ApiModelProperty(value="exception异常")
    private String exception;

    /**
     * 总消耗时间
     */
    @Column(name = "total_times")
    @ApiModelProperty(value="totalTimes总消耗时间")
    private Long totalTimes;

    /**
     * 数据库访问时间
     */
    @Column(name = "db_times")
    @ApiModelProperty(value="dbTimes数据库访问时间")
    private Long dbTimes;

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public AccessLog setId(Long id) {
        this.id = id;
        return this;
    }

    /**
     * 获取日志级别
     *
     * @return level - 日志级别
     */
    public String getLevel() {
        return level;
    }

    /**
     * 设置日志级别
     *
     * @param level 日志级别
     */
    public AccessLog setLevel(String level) {
        this.level = level;
        return this;
    }

    /**
     * 获取日志标题
     *
     * @return title - 日志标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置日志标题
     *
     * @param title 日志标题
     */
    public AccessLog setTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * 获取请求ip地址
     *
     * @return ip - 请求ip地址
     */
    public String getIp() {
        return ip;
    }

    /**
     * 设置请求ip地址
     *
     * @param ip 请求ip地址
     */
    public AccessLog setIp(String ip) {
        this.ip = ip;
        return this;
    }

    /**
     * 获取请求方式
     *
     * @return method - 请求方式
     */
    public String getMethod() {
        return method;
    }

    /**
     * 设置请求方式
     *
     * @param method 请求方式
     */
    public AccessLog setMethod(String method) {
        this.method = method;
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
    public AccessLog setVersion(Integer version) {
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
    public AccessLog setCreatedBy(Long createdBy) {
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
    public AccessLog setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
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
    public AccessLog setModifiedBy(Long modifiedBy) {
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
    public AccessLog setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
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
    public AccessLog setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
        return this;
    }

    /**
     * 获取用户openid
     *
     * @return url - 用户openid
     */
    public String getUrl() {
        return url;
    }

    /**
     * 设置用户openid
     *
     * @param url 用户openid
     */
    public AccessLog setUrl(String url) {
        this.url = url;
        return this;
    }

    /**
     * 获取提交参数
     *
     * @return params - 提交参数
     */
    public String getParams() {
        return params;
    }

    /**
     * 设置提交参数
     *
     * @param params 提交参数
     */
    public AccessLog setParams(String params) {
        this.params = params;
        return this;
    }

    /**
     * 获取异常
     *
     * @return exception - 异常
     */
    public String getException() {
        return exception;
    }

    /**
     * 设置异常
     *
     * @param exception 异常
     */
    public AccessLog setException(String exception) {
        this.exception = exception;
        return this;
    }

    /**
     * 获取总消耗时间
     *
     * @return total_times - 总消耗时间
     */
    public Long getTotalTimes() {
        return totalTimes;
    }

    /**
     * 设置总消耗时间
     *
     * @param totalTimes 总消耗时间
     */
    public AccessLog setTotalTimes(Long totalTimes) {
        this.totalTimes = totalTimes;
        return this;
    }

    /**
     * 获取数据库访问时间
     *
     * @return db_times - 数据库访问时间
     */
    public Long getDbTimes() {
        return dbTimes;
    }

    /**
     * 设置数据库访问时间
     *
     * @param dbTimes 数据库访问时间
     */
    public AccessLog setDbTimes(Long dbTimes) {
        this.dbTimes = dbTimes;
        return this;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Override
    public String toString() {
        return "AccessLog{" +
                "id=" + id +
                ", level='" + level + '\'' +
                ", title='" + title + '\'' +
                ", ip='" + ip + '\'' +
                ", url=["+method+"]'" + url + '\'' +
                ", params='" + params + '\'' +
                ", exception='" + exception + '\'' +
                ", totalTimes=" + totalTimes +
                ", dbTimes=" + dbTimes +
                ", createdBy=" + createdBy +
                ", createdByName='" + createdByName + '\'' +
                ", createdTime=" + createdTime +
                '}';
    }
}