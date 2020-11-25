package cn.watsontech.webhelper.utils.result;

import java.util.List;

/**
 * Created by Watson on 2020/11/25.
 */
public interface ResultPage<T> extends List {

    List<T> getResult();
    Number getStartRow();
    Number getPageSize();
    Number getTotal();
}
