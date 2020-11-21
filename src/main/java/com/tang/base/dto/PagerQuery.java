package com.tang.base.dto;

import com.tang.base.consts.BaseConsts;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * 
 * @ClassName PagerQuery
 * @Description 分页抽象类(具体的分页查询请求参数类均继承此类)
 */
@ApiModel(value = "PagerQuery",description = "分页抽象类")
public abstract class PagerQuery {

    @ApiModelProperty(value = "当前页", required = true, position = 0)
    @NotNull(message = "当前页码不能为空")
    protected Integer page;
    
    @ApiModelProperty(value = "每页显示数量", required = true, position = 10)
    @NotNull(message = "当前页条数不能为空")
    protected Integer limit;

    public Integer getPage() {
        return page;
    }
    
    public void setPage(Integer page) {
        this.page = page == null || page == 0 ? BaseConsts.DEFAULT_PAGE : page;
    }
    
    public Integer getLimit() {
        return limit;
    }
    
    public void setLimit(Integer limit) {
        this.limit = limit == null || limit == 0 ? BaseConsts.DEFAULT_PAGE_SIZE : limit;
    }
    
}
