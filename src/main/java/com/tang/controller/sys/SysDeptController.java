package com.tang.controller.sys;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tang.base.annotation.SysLog;
import com.tang.base.consts.BaseConsts;
import com.tang.base.dto.BaseIDTrueDTO;
import com.tang.base.dto.RR;
import com.tang.base.enums.LogEnum;
import com.tang.base.exception.ServerException;
import com.tang.base.validator.Create;
import com.tang.base.validator.Update;
import com.tang.controller.BaseController;
import com.tang.dto.AddDeptDTO;
import com.tang.dto.DeptDTO;
import com.tang.entity.SysDeptEntity;
import com.tang.entity.SysRoleEntity;
import com.tang.service.SysDeptService;
import com.tang.service.SysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Date;

/**
 * <p>
 * 部门表 与角色关联 前端控制器
 * </p>
 *
 * @author 芙蓉王
 * @since 2020-11-16
 */
@RestController
@RequestMapping("/sys/dept")
@Api(value = "系统管理-部门管理接口", tags = "系统管理-部门管理接口")
public class SysDeptController extends BaseController {

    @Autowired
    private SysDeptService sysDeptService;

    @Autowired
    private SysRoleService sysRoleService;

    @PreAuthorize("hasAuthority('sys:dept:page')")
    @PostMapping(value = "/page", produces = BaseConsts.REQUEST_HEADERS_CONTENT_TYPE)
    @ApiOperation(value = "部门管理-部门列表", notes = "菜单管理-部门列表", httpMethod = BaseConsts.REQUEST_METHOD, response = RR.class)
    public RR page(@Validated @RequestBody DeptDTO dto) {
        return RR.success("获取部门列表成功", sysDeptService.page(new Page(dto.getPage(), dto.getLimit())));
    }


    @SysLog(logEnum = LogEnum.ADD_DEPT)
    @PreAuthorize("hasAuthority('sys:dept:add')")
    @PostMapping(value = "/add", produces = BaseConsts.REQUEST_HEADERS_CONTENT_TYPE)
    @ApiOperation(value = "部门管理-新增部门", notes = "菜单管理-新增部门", httpMethod = BaseConsts.REQUEST_METHOD, response = RR.class)
    public RR add(@Validated(Create.class) @RequestBody AddDeptDTO dto) {
        SysDeptEntity data = new SysDeptEntity();
        BeanUtils.copyProperties(dto, data);

        Date date = new Date();
        return RR.success("新增成功", sysDeptService.save(data.setCreateTime(date).setUpdateTime(date)));
    }

    @SysLog(logEnum = LogEnum.EDIT_DEPT)
    @PreAuthorize("hasAuthority('sys:dept:update')")
    @PostMapping(value = "/update", produces = BaseConsts.REQUEST_HEADERS_CONTENT_TYPE)
    @ApiOperation(value = "部门管理-编辑部门", notes = "菜单管理-编辑部门", httpMethod = BaseConsts.REQUEST_METHOD, response = RR.class)
    public RR update(@Validated(Update.class) @RequestBody AddDeptDTO dto) throws Exception {
        SysDeptEntity dept = sysDeptService.getById(dto.getId());
        if (dept == null) throw new ServerException("非法ID,查询部门信息不存在");

        SysDeptEntity data = new SysDeptEntity().setId(dept.getId()).setName(dto.getName()).setRmk(dto.getRmk()).setUpdateTime(new Date());
        return RR.success("修改成功", sysDeptService.updateById(data));
    }


    @SysLog(logEnum = LogEnum.DELETE_DEPT)
    @PreAuthorize("hasAuthority('sys:dept:delete')")
    @PostMapping(value = "/delete", produces = BaseConsts.REQUEST_HEADERS_CONTENT_TYPE)
    @ApiOperation(value = "部门管理-删除部门", notes = "菜单管理-删除部门", httpMethod = BaseConsts.REQUEST_METHOD, response = RR.class)
    public RR delete(@Validated @RequestBody BaseIDTrueDTO dto) throws Exception {
        SysDeptEntity dept = sysDeptService.getById(dto.getId());
        if (dept == null) throw new ServerException("非法ID,查询部门信息不存在");

        if (sysRoleService.count(new LambdaQueryWrapper<SysRoleEntity>().eq(SysRoleEntity::getDeptId, dept.getId())) > 0)
            throw new ServerException("改部门下存在角色,无法删除");
        return RR.success("删除成功", sysDeptService.removeById(dept.getId()));
    }


}
