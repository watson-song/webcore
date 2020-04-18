package cn.watsontech.core.mybatis.mapper;

/**
 * Copyright to watsontech
 * OGNL静态方法
 */
public class BatchInsertOgnl {

    /**
     * 是否包含自定义插入列
     *
     * @param parameter
     * @return
     */
    public static boolean hasInsertSelectiveColumns(Object parameter) {
        if (parameter != null && parameter instanceof BatchInsertModel) {
            BatchInsertModel model = (BatchInsertModel) parameter;
            if (model.getInsertColumns() != null && model.getInsertColumns().size() > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否包含自定义插入列
     *
     * @param parameter
     * @return
     */
    public static boolean hasInsertSelectiveColumn(Object parameter, String column) {
        if (parameter != null && parameter instanceof BatchInsertModel) {
            BatchInsertModel model = (BatchInsertModel) parameter;
            if (model.getInsertColumns() != null && model.getInsertColumns().size() > 0) {
                return model.getInsertColumns().contains(column);
            }
        }
        return false;
    }
}
