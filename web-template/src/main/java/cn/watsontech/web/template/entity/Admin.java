/*** @generatedBy Watson WebCore 2020-11-30 11:44:52*/
package cn.watsontech.web.template.entity;

import cn.watsontech.webhelper.common.security.IUserType;
import cn.watsontech.webhelper.common.security.LoginUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.Date;

@ApiModel(value="Admin")
@Table(name = "tb_admin")
public class Admin extends LoginUser implements cn.watsontech.webhelper.utils.mybatis.CreatedEntity<Admin, Long, Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value="id")
    private Long id;

    @ApiModelProperty(value="username")
    private String username;

    @ApiModelProperty(value="password")
    private String password;

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

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    @Override
    public IUserType getUserType() {
        return LoginUser.Type.admin;
    }

    @Override
    public String getMobile() {
        return null;
    }

    @Override
    public Boolean getExpired() {
        return false;
    }

    @Override
    public Boolean getLocked() {
        return false;
    }

    @Override
    public Boolean getCredentialsExpired() {
        return false;
    }

    /**
     * @param id
     */
    public Admin setId(Long id) {
        this.id = id;
        return this;
    }

    /**
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username
     */
    public Admin setUsername(String username) {
        this.username = username;
        return this;
    }

    /**
     * @return password
     */
    public String getPassword() {
        return password;
    }

    @Override
    public Boolean getEnabled() {
        return true;
    }

    @Override
    public String getNickName() {
        return "";
    }

    @Override
    public String getAvatarUrl() {
        return null;
    }

    /**
     * @param password
     */
    public Admin setPassword(String password) {
        this.password = password;
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
    public Admin setCreatedTime(Date createdTime) {
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
    public Admin setCreatedBy(Long createdBy) {
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
    public Admin setCreatedByName(String createdByName) {
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
    public Admin setModifiedTime(Date modifiedTime) {
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
    public Admin setModifiedBy(Long modifiedBy) {
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
    public Admin setVersion(Integer version) {
        this.version = version;
        return this;
    }
}