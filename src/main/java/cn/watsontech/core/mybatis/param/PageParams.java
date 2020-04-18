package cn.watsontech.core.mybatis.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;

import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * Created by Watson on 2019/12/18.
 */
@Data
public class PageParams {

    @ApiModelProperty(value = "分页码", example="1")
    @NotNull(message = "p参数(page)不能为空")
    protected Integer p = 1;  //page 默认第一页

    @ApiModelProperty(value = "页面大小，默认20", example="20")
    @NotNull(message = "ps参数(pageSize)不能为空")
    protected Integer ps = 20; //pageSize 默认每页10条

    @ApiModelProperty(value = "排序sortBy", example="createdTime")
    protected String sby;  //sortBy

    @ApiModelProperty(value = "升降序", example="desc")
    protected String ord; //order

    /**
     * @deprecated("使用getOrderByClause(Class entityClass)方法")
     */
    @ApiModelProperty(value = "排序合计", example="desc", hidden = true)
    public String getOrderByClause() {
        return null;
    }

    public String getOrderByClause(Class entityClass) {
        if (!StringUtils.isEmpty(getSby())) {
            //分割点好 a.createdTime
            String[] sbySplit = getSby().split(".");
            String prefix = "";
            if (sbySplit.length>1) {
                prefix = sbySplit[0]+".";
            }
            String column = getColumn(entityClass, getSby());
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
//
//    public long getTotal(Long recordCount) {
//        if (recordCount < 1) {
//            return 0;
//        }
//        return (recordCount / ps) + 1;
//    }
//
    public int getOffset() {
        if (p>0) return (p-1)*ps;

        return 0;
    }
}