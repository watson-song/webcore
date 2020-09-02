package cn.watsontech.core.web.result;

import lombok.Data;

import java.util.List;

/**
 * Created by Watson on 2019/12/20.
 */
@Data
public class ResultList<T> {
    Boolean hasNext;
    Long offset;
    Integer limit;
    Long total;
    List<T> list;

    public ResultList(List<T> list) {
        this.list = list;
        this.total = this.list!=null?this.list.size():0l;
        this.hasNext = calHasNext();
    }

    public ResultList(List<T> list, Long offset, Integer limit) {
        this.offset = offset;
        this.limit = limit;
        this.list = list;
        this.total = list!=null?list.size():0l;
        this.hasNext = calHasNext();
    }

    public ResultList(List<T> list, Long offset, Integer limit, Long total) {
        this.offset = offset;
        this.limit = limit;
        this.total = total;
        this.list = list;
        this.hasNext = calHasNext();
    }

    private boolean calHasNext() {
        if (list!=null&&offset!=null&&total!=null) {
            return offset+list.size()<total;
        }

        return false;
    }
}
