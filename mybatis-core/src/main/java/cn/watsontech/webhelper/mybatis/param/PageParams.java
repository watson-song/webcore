package cn.watsontech.webhelper.mybatis.param;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;

import java.util.Set;

/**
 * Created by Watson on 2019/12/18.
 */
public class PageParams {

    public PageParams() {}
    public PageParams(Integer offset, Integer limit) {
        this.offset = offset;
        this.limit = limit;
    }

//    @ApiModelProperty(value = "分页码", example="1")
//    protected Integer p = 1;  //page 默认第一页
//
//    @ApiModelProperty(value = "页面大小，默认20", example="20")
//    protected Integer ps = 20; //pageSize 默认每页10条

    @ApiModelProperty(value = "分页码", example="0")
    protected Integer offset;  //从0开始，默认为空，若传该参数则使用该值，若未传则使用p，默认offset为0，p为1

    @ApiModelProperty(value = "页面大小，默认20", example="20")
    protected Integer limit; //默认为空，若传该参数则使用该值，若未传则使用ps，默认size为20

    @ApiModelProperty(value = "排序sortBy", example="createdTime")
    protected String sby;  //sortBy

    @ApiModelProperty(value = "升降序", example="desc")
    protected String ord; //order

    /**
     * 提供前端支持 p和ps参数
     */
    public Integer getOffset() {
        if (offset==null) {
//            if (p!=null) {
//                if (p<1) p =1;
//
//                return (p-1)*getLimit();
//            }
            return 0;
        }

        return offset>0?offset:0;
    }

    /**
     * 提供前端支持 p和ps参数
     * 注意：分页大小优先使用limit参数
     */
    public Integer getLimit() {
        if (limit==null) {
//            if (getPs()!=null) {
//                return getPs();
//            }
            return 10;
        }

        return limit>0?limit:0;
    }

    /**
     * @deprecated("使用getOrderByClause(Class entityClass)方法")
     */
    @ApiModelProperty(value = "排序合计", example="desc", hidden = true)
    public String getOrderByClause() {
        return null;
    }

    public String getOrderByClause(Class entityClass) {
        String property = getSby();
        if (!StringUtils.isEmpty(property)) {
            //分割点好 a.createdTime
            String[] sbySplit = property.split("\\.");
            String prefix = "";
            if (sbySplit.length>1) {
                prefix = sbySplit[0]+".";
                Assert.isTrue(sbySplit.length==2, "排序不支持多层级属性");
                property = sbySplit[1];
            }

            String column = getColumn(entityClass, property);
            if (!StringUtils.isEmpty(getOrd())) {
                if (column!=null) {
                    return prefix + column +" "+ getOrd();
                }
            }

            return column;
        }

        return null;
    }

    private String getColumn(Class entityClass, String property) {
        Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        for (EntityColumn column : columnList) {
            if (column.getProperty().equals(property)) {
                return column.getColumn();
            }
        }

        return null;
    }

//    public Integer getP() {
//        return p;
//    }
//
//    public void setP(Integer p) {
//        this.p = p;
//    }
//
//    public Integer getPs() {
//        return ps;
//    }
//
//    public void setPs(Integer ps) {
//        this.ps = ps;
//    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public String getSby() {
        return sby;
    }

    public void setSby(String sby) {
        this.sby = sby;
    }

    public String getOrd() {
        return ord;
    }

    public void setOrd(String ord) {
        this.ord = ord;
    }
}
