package com.tang.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tang.base.dto.PagerQuery;
import lombok.Data;

import java.util.List;

/**
 * 分页实体类
 */
@Data
public class PageInfo<T> {

    /**
     * 默认显示的记录数
     */
//    private final static int PAGESIZE = 10;

    /**
     * 总记录条数
     */
    private int total;

    /**
     * 返回数据集合
     */
    private List<T> records;

    @JsonIgnore
    private int from;
    @JsonIgnore
    private int size;

    /**
     * 当前页
     */
    @JsonIgnore
    private int nowpage;

    /**
     * 每页显示的记录数
     */
    @JsonIgnore
    private int pagesize;

    /**
     * 查询条件
     */
    @JsonIgnore
    private PagerQuery condition;

    public PageInfo() {}

//    //构造方法
//    public PageInfo(int nowpage, int pagesize) {
//        //计算当前页
//        if (nowpage < 0) {
//            this.nowpage = 1;
//        } else {
//            //当前页
//            this.nowpage = nowpage;
//        }
//        //记录每页显示的记录数
//        if (pagesize < 0) {
//            this.pagesize = PAGESIZE;
//        } else {
//        	this.pagesize = pagesize;
//        }
//        //计算开始的记录和结束的记录
//        this.from = (this.nowpage - 1) * this.pagesize;
//        this.size = this.pagesize;
//    }

    //构造方法
    public PageInfo(PagerQuery condition) {
        //当前页
        this.nowpage = condition.getPage();
        //记录每页显示的记录数
        this.pagesize = condition.getLimit();

        //计算开始的记录和结束的记录
        this.from = (this.nowpage - 1) * this.pagesize;
        this.size = this.pagesize;
        this.condition = condition;
    }
}
