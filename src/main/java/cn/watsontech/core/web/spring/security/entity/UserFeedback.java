package cn.watsontech.core.web.spring.security.entity;

import cn.watsontech.core.mybatis.CreatedEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@ApiModel(value="cn.watsontech.core.security.entity.UserFeedback")
@Table(name = "tb_user_feedback")
public class UserFeedback implements CreatedEntity<UserFeedback, Long, Long> {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value="id")
    private Long id;

    @Column(name = "user_id")
    @ApiModelProperty(value="userId")
    private Long userId;

    /**
     * 用户类型
     */
    @Column(name = "user_type")
    @ApiModelProperty(value="userType用户类型")
    private String userType;

    /**
     * 图片1
     */
    @Column(name = "image1_url")
    @ApiModelProperty(value="image1Url图片1")
    private String image1Url;

    /**
     * 图片2
     */
    @Column(name = "image2_url")
    @ApiModelProperty(value="image2Url图片2")
    private String image2Url;

    /**
     * 图片3
     */
    @Column(name = "image3_url")
    @ApiModelProperty(value="image3Url图片3")
    private String image3Url;

    /**
     * 反馈内容
     */
    @ApiModelProperty(value="content反馈内容")
    private String content;

    /**
     * 联系方式
     */
    @ApiModelProperty(value="telephone联系方式")
    private String telephone;

    /**
     * 订单号
     */
    @Column(name = "order_id")
    @ApiModelProperty(value="orderId订单号")
    private Long orderId;

    @ApiModelProperty(value="state")
    private String state;

    /**
     * 创建人
     */
    @Column(name = "created_by")
    @ApiModelProperty(value="createdBy创建人")
    private Long createdBy;

    /**
     * 创建人名称
     */
    @Column(name = "created_by_name")
    @ApiModelProperty(value="createdByName创建人名称")
    private String createdByName;

    /**
     * 创建时间
     */
    @Column(name = "created_time")
    @ApiModelProperty(value="createdTime创建时间")
    private Date createdTime;

    /**
     * 修改人
     */
    @Column(name = "modified_by")
    @ApiModelProperty(value="modifiedBy修改人")
    private String modifiedBy;

    /**
     * 修改时间
     */
    @Column(name = "modified_time")
    @ApiModelProperty(value="modifiedTime修改时间")
    private Date modifiedTime;

    @ApiModelProperty(value="version")
    private Integer version;

    /**
     * 后台审核反馈内容
     */
    @Column(name = "review_notes")
    @ApiModelProperty(value="reviewNotes后台审核反馈内容")
    private String reviewNotes;

    /**
     * 审核人
     */
    @Column(name = "reviewer_id")
    @ApiModelProperty(value="reviewerId审核人")
    private Long reviewerId;

    @Column(name = "review_time")
    @ApiModelProperty(value="reviewTime")
    private Date reviewTime;

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public UserFeedback setId(Long id) {
        this.id = id;
        return this;
    }

    /**
     * @return user_id
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * @param userId
     */
    public UserFeedback setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    /**
     * 获取用户类型
     *
     * @return user_type - 用户类型
     */
    public String getUserType() {
        return userType;
    }

    /**
     * 设置用户类型
     *
     * @param userType 用户类型
     */
    public UserFeedback setUserType(String userType) {
        this.userType = userType;
        return this;
    }

    /**
     * 获取图片1
     *
     * @return image1_url - 图片1
     */
    public String getImage1Url() {
        return image1Url;
    }

    /**
     * 设置图片1
     *
     * @param image1Url 图片1
     */
    public UserFeedback setImage1Url(String image1Url) {
        this.image1Url = image1Url;
        return this;
    }

    /**
     * 获取图片2
     *
     * @return image2_url - 图片2
     */
    public String getImage2Url() {
        return image2Url;
    }

    /**
     * 设置图片2
     *
     * @param image2Url 图片2
     */
    public UserFeedback setImage2Url(String image2Url) {
        this.image2Url = image2Url;
        return this;
    }

    /**
     * 获取图片3
     *
     * @return image3_url - 图片3
     */
    public String getImage3Url() {
        return image3Url;
    }

    /**
     * 设置图片3
     *
     * @param image3Url 图片3
     */
    public UserFeedback setImage3Url(String image3Url) {
        this.image3Url = image3Url;
        return this;
    }

    /**
     * 获取反馈内容
     *
     * @return content - 反馈内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置反馈内容
     *
     * @param content 反馈内容
     */
    public UserFeedback setContent(String content) {
        this.content = content;
        return this;
    }

    /**
     * 获取联系方式
     *
     * @return telephone - 联系方式
     */
    public String getTelephone() {
        return telephone;
    }

    /**
     * 设置联系方式
     *
     * @param telephone 联系方式
     */
    public UserFeedback setTelephone(String telephone) {
        this.telephone = telephone;
        return this;
    }

    /**
     * 获取订单号
     *
     * @return order_id - 订单号
     */
    public Long getOrderId() {
        return orderId;
    }

    /**
     * 设置订单号
     *
     * @param orderId 订单号
     */
    public UserFeedback setOrderId(Long orderId) {
        this.orderId = orderId;
        return this;
    }

    /**
     * @return state
     */
    public String getState() {
        return state;
    }

    /**
     * @param state
     */
    public UserFeedback setState(String state) {
        this.state = state;
        return this;
    }

    /**
     * 获取创建人
     *
     * @return created_by - 创建人
     */
    public Long getCreatedBy() {
        return createdBy;
    }

    /**
     * 设置创建人
     *
     * @param createdBy 创建人
     */
    @Override
    public UserFeedback setCreatedBy(Long createdBy) {
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
    public UserFeedback setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
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
    public UserFeedback setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
        return this;
    }

    /**
     * 获取修改人
     *
     * @return modified_by - 修改人
     */
    public String getModifiedBy() {
        return modifiedBy;
    }

    /**
     * 设置修改人
     *
     * @param modifiedBy 修改人
     */
    public UserFeedback setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
        return this;
    }

    /**
     * 获取修改时间
     *
     * @return modified_time - 修改时间
     */
    public Date getModifiedTime() {
        return modifiedTime;
    }

    /**
     * 设置修改时间
     *
     * @param modifiedTime 修改时间
     */
    public UserFeedback setModifiedTime(Date modifiedTime) {
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
    public UserFeedback setVersion(Integer version) {
        this.version = version;
        return this;
    }

    /**
     * 获取后台审核反馈内容
     *
     * @return review_notes - 后台审核反馈内容
     */
    public String getReviewNotes() {
        return reviewNotes;
    }

    /**
     * 设置后台审核反馈内容
     *
     * @param reviewNotes 后台审核反馈内容
     */
    public UserFeedback setReviewNotes(String reviewNotes) {
        this.reviewNotes = reviewNotes;
        return this;
    }

    /**
     * 获取审核人
     *
     * @return reviewer_id - 审核人
     */
    public Long getReviewerId() {
        return reviewerId;
    }

    /**
     * 设置审核人
     *
     * @param reviewerId 审核人
     */
    public UserFeedback setReviewerId(Long reviewerId) {
        this.reviewerId = reviewerId;
        return this;
    }

    /**
     * @return review_time
     */
    public Date getReviewTime() {
        return reviewTime;
    }

    /**
     * @param reviewTime
     */
    public UserFeedback setReviewTime(Date reviewTime) {
        this.reviewTime = reviewTime;
        return this;
    }
}