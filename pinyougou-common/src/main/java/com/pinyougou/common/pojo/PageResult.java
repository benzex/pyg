package com.pinyougou.common.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * 分页实体类(封装分页数据)
 *
 * @author lee.siu.wah
 * @version 1.0
 * <p>File Created at 2019-03-29<p>
 */
public class PageResult implements Serializable{

    // 总记录数
    private long total;
    // 分页数据
    private List<?> rows;

    public PageResult(){

    }
    public PageResult(long total, List<?> rows) {
        this.total = total;
        this.rows = rows;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<?> getRows() {
        return rows;
    }

    public void setRows(List<?> rows) {
        this.rows = rows;
    }
}
