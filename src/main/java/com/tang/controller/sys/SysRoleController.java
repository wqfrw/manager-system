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
import com.tang.dto.AddRoleDTO;
import com.tang.dto.RoleAuthorDTO;
import com.tang.dto.RoleDTO;
import com.tang.entity.*;
import com.tang.service.SysDeptService;
import com.tang.service.SysRoleMenuService;
import com.tang.service.SysRoleService;
import com.tang.service.SysUserRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author 芙蓉王
 * @since 2020-11-16
 */
@RestController
@RequestMapping("/sys/role")
@Api(value = "系统管理-角色管理接口", tags = "系统管理-角色管理接口")
public class SysRoleController extends BaseController {

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysDeptService sysDeptService;

    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    @PreAuthorize("hasAuthority('sys:role:page')")
    @PostMapping(value = "/page", produces = BaseConsts.REQUEST_HEADERS_CONTENT_TYPE)
    @ApiOperation(value = "角色管理-角色列表", notes = "角色管理-角色列表", httpMethod = BaseConsts.REQUEST_METHOD, response = RR.class)
    public RR page(@Validated @RequestBody RoleDTO dto) {
        LambdaQueryWrapper<SysRoleEntity> lqw = new LambdaQueryWrapper<>();
        lqw.eq(StringUtils.isNotBlank(dto.getName()), SysRoleEntity::getName, dto.getName())
                .eq(!isAdmin(), SysRoleEntity::getDeptId, getSecurityUser().getDept().getId());

        Page<SysRoleEntity> page = sysRoleService.page(new Page(dto.getPage(), dto.getLimit()), lqw);

        //如果是管理员  则查询出部门名匹配进去  反之直接取当前登录用户的部门名赋值
        if (isAdmin()) {
            List<Long> deptIds = page.getRecords().stream().map(SysRoleEntity::getDeptId).distinct().collect(Collectors.toList());
            List<SysDeptEntity> deptList = sysDeptService.list(new LambdaQueryWrapper<SysDeptEntity>().eq(SysDeptEntity::getId, deptIds));
            page.getRecords().forEach(v -> v.setDeptName(deptList.stream().filter(d -> d.getId().equals(v.getDeptId())).findFirst().get().getName()));
        } else {
            String deptName = getSecurityUser().getDept().getName();
            page.getRecords().forEach(v -> v.setDeptName(deptName));
        }
        return RR.success("分页查询角色列表成功", page);
    }

    @PreAuthorize("hasAuthority('sys:user:add') || hasAuthority('sys:user:update')")
    @PostMapping(value = "/select", produces = BaseConsts.REQUEST_HEADERS_CONTENT_TYPE)
    @ApiOperation(value = "角色管理-角色选择下拉框", notes = "角色管理-角色选择下拉框", httpMethod = BaseConsts.REQUEST_METHOD, response = RR.class)
    public RR select() {
        LambdaQueryWrapper<SysRoleEntity> lqw = new LambdaQueryWrapper<>();
        //如果是管理员  查询所有角色   反之查询自身部门下的角色
        lqw.eq(!isAdmin(), SysRoleEntity::getDeptId, getSecurityUser().getDept().getId())
                .eq(SysRoleEntity::getStatus, SysRoleEntity.NORMAL_STATUS)
                .select(SysRoleEntity::getId, SysRoleEntity::getName);
        return RR.success("查询角色下拉列表成功", sysRoleService.list(lqw));
    }

    @SysLog(logEnum = LogEnum.ADD_ROLE)
    @PreAuthorize("hasAuthority('sys:role:add')")
    @PostMapping(value = "/add", produces = BaseConsts.REQUEST_HEADERS_CONTENT_TYPE)
    @ApiOperation(value = "角色管理-添加角色", notes = "角色管理-添加角色", httpMethod = BaseConsts.REQUEST_METHOD, response = RR.class)
    public RR add(@Validated(Create.class) @RequestBody AddRoleDTO dto) throws Exception {
        if (isAdmin()) {
            SysDeptEntity dept = sysDeptService.getById(dto.getDeptId());
            if (dept == null) throw new ServerException("参数错误,查询部门信息不存在");
        } else {
            dto.setDeptId(getSecurityUser().getDept().getId());
        }
        SysRoleEntity one = sysRoleService.getOne(new LambdaQueryWrapper<SysRoleEntity>().eq(SysRoleEntity::getName, dto.getName()).eq(SysRoleEntity::getDeptId, dto.getDeptId()));
        if (one != null) throw new ServerException("该部门下已经存在该角色名!");

        SysRoleEntity data = new SysRoleEntity();
        BeanUtils.copyProperties(dto, data);

        Date date = new Date();
        sysRoleService.save(data.setId(null).setCreateTime(date).setUpdateTime(date));
        return RR.success("新增角色信息成功");
    }


    @SysLog(logEnum = LogEnum.EDIT_ROLE)
    @PreAuthorize("hasAuthority('sys:role:update')")
    @PostMapping(value = "/update", produces = BaseConsts.REQUEST_HEADERS_CONTENT_TYPE)
    @ApiOperation(value = "角色管理-编辑角色", notes = "角色管理-编辑角色", httpMethod = BaseConsts.REQUEST_METHOD, response = RR.class)
    public RR update(@Validated(Update.class) @RequestBody AddRoleDTO dto) throws Exception {
        SysRoleEntity role = sysRoleService.getById(dto.getId());
        if (role == null) throw new ServerException("非法ID,查询角色信息不存在");
        if (!isAdmin() && !role.getDeptId().equals(getSecurityUser().getDept().getId()))
            throw new ServerException("非法部门ID,该部门非当前登录用户所属部门");

        SysRoleEntity date = new SysRoleEntity().setId(dto.getId())
                .setCode(dto.getCode())
                .setName(dto.getName())
                .setStatus(dto.getStatus())
                .setDeptId(dto.getDeptId())
                .setUpdateTime(new Date());
        sysRoleService.updateById(date);
        return RR.success("更新角色信息成功");
    }


    @SysLog(logEnum = LogEnum.DELETE_ROLE)
    @PreAuthorize("hasAuthority('sys:role:delete')")
    @PostMapping(value = "/delete", produces = BaseConsts.REQUEST_HEADERS_CONTENT_TYPE)
    @ApiOperation(value = "角色管理-删除角色", notes = "角色管理-删除角色", httpMethod = BaseConsts.REQUEST_METHOD, response = RR.class)
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public RR delete(@Validated @RequestBody BaseIDTrueDTO dto) throws Exception {
        SysRoleEntity role = sysRoleService.getById(dto.getId());
        if (role == null) throw new ServerException("非法ID,查询角色信息不存在");
        if (!isAdmin() && !role.getDeptId().equals(getSecurityUser().getDept().getId()))
            throw new ServerException("非法部门ID,该部门非当前登录用户所属部门");

        if (sysUserRoleService.count(new LambdaQueryWrapper<SysUserRoleEntity>().eq(SysUserRoleEntity::getRoleId, role.getId())) > 0)
            throw new ServerException("该角色下存在用户,无法删除");

        sysRoleService.removeById(role.getId());
        sysRoleMenuService.remove(new LambdaQueryWrapper<SysRoleMenuEntity>().eq(SysRoleMenuEntity::getRoleId, role.getId()));
        return RR.success("删除角色信息成功");
    }

    @SysLog(logEnum = LogEnum.AUTH_ROLE)
    @PreAuthorize("hasAuthority('sys:role:authorize')")
    @PostMapping(value = "/author", produces = BaseConsts.REQUEST_HEADERS_CONTENT_TYPE)
    @ApiOperation(value = "角色管理-角色授权", notes = "角色管理-角色授权", httpMethod = BaseConsts.REQUEST_METHOD, response = RR.class)
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public RR author(@Validated @RequestBody RoleAuthorDTO dto) throws Exception {
        //只有管理员能授权
        if (!isAdmin()) throw new ServerException("非法操作,非系统管理员无法授权");

        //先删除该角色所有对应的菜单
        sysRoleMenuService.remove(new LambdaQueryWrapper<SysRoleMenuEntity>().eq(SysRoleMenuEntity::getRoleId, dto.getRoleId()));

        if (!CollectionUtils.isEmpty(dto.getMenuIds())) {
            Date date = new Date();
            List<SysRoleMenuEntity> roleMenuList = dto.getMenuIds().stream().map(v -> new SysRoleMenuEntity()
                    .setRoleId(dto.getRoleId())
                    .setMenuId(v)
                    .setUsername(getUsername())
                    .setCreateTime(date)
                    .setUpdateTime(date)
            ).collect(Collectors.toList());

            //然后插入所有新增的菜单
            sysRoleMenuService.saveBatch(roleMenuList);
        }
        return RR.success("授权成功");
    }


}
