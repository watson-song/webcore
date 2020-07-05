package cn.watsontech.core.web.result;

import lombok.Data;

import java.util.List;

/**
 * Created by Watson on 2019/12/20.
 */
@Data
public class ResultList<T> {
    Integer page;
    Integer offset;
    Integer limit;
    Long total;
    List<T> list;

    public ResultList(List<T> list) {
        this.list = list;
        this.total = this.list!=null?this.list.size():0l;
    }

    public ResultList(List<T> list, Integer offset, Integer limit) {
        this.offset = offset;
        this.limit = limit;
        this.list = list;
        this.total = list!=null?list.size():0l;
        this.page = offset/limit;
    }

    public ResultList(List<T> list, Integer offset, Integer limit, Long total) {
        this.page = offset/limit;
        this.offset = offset;
        this.limit = limit;
        this.total = total;
        this.list = list;
    }
}
