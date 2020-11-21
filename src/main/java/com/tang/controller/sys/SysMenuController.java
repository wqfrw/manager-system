package com.tang.controller.sys;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tang.base.annotation.SysLog;
import com.tang.base.consts.BaseConsts;
import com.tang.base.dto.BaseIDTrueDTO;
import com.tang.base.dto.RR;
import com.tang.base.enums.LogEnum;
import com.tang.base.exception.ServerException;
import com.tang.base.validator.Create;
import com.tang.base.validator.Update;
import com.tang.controller.BaseController;
import com.tang.dto.AddMenuDTO;
import com.tang.entity.SysMenuEntity;
import com.tang.entity.SysRoleMenuEntity;
import com.tang.service.SysMenuService;
import com.tang.service.SysRoleMenuService;
import com.tang.utils.TreeBuilder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
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
 * 菜单管理表 前端控制器
 * </p>
 *
 * @author 芙蓉王
 * @since 2020-11-16
 */
@RestController
@RequestMapping("/sys/menu")
@Api(value = "系统管理-菜单管理接口", tags = "系统管理-菜单管理接口")
public class SysMenuController extends BaseController {

    @Autowired
    private SysMenuService sysMenuService;

    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    @PreAuthorize("hasAuthority('sys:role:authorize') || hasAuthority('sys:menu:tree')")
    @PostMapping(value = "/menuTree", produces = BaseConsts.REQUEST_HEADERS_CONTENT_TYPE)
    @ApiOperation(value = "菜单管理-获取所有菜单树", notes = "菜单管理-获取所有菜单树", httpMethod = BaseConsts.REQUEST_METHOD, response = RR.class)
    public RR menuTree() {
        //双重限制   只有管理员能授权 非管理员直接返回空列表
        if (!isAdmin()) return RR.success();

        //获取所有开启的菜单
        List<SysMenuEntity> menuList = sysMenuService.list(new LambdaQueryWrapper<SysMenuEntity>().eq(SysMenuEntity::getStatus, SysMenuEntity.NORMAL_STATUS));
        return RR.success("获取所有菜单树成功", TreeBuilder.buildMenu(menuList));
    }

    @PreAuthorize("hasAuthority('sys:role:authorize')")
    @PostMapping(value = "/getMenuByRoleId", produces = BaseConsts.REQUEST_HEADERS_CONTENT_TYPE)
    @ApiOperation(value = "菜单管理-获取角色对应菜单", notes = "菜单管理-获取角色对应菜单", httpMethod = BaseConsts.REQUEST_METHOD, response = RR.class)
    public RR getMenuByRoleId(@Validated @RequestBody BaseIDTrueDTO dto) throws Exception {
        //只有管理员能授权
        if (!isAdmin()) throw new ServerException("非法操作,非系统管理员无法授权");

        //先查询出该角色对应的菜单
        List<SysRoleMenuEntity> roleMenuList = sysRoleMenuService.list(new LambdaQueryWrapper<SysRoleMenuEntity>().eq(SysRoleMenuEntity::getRoleId, dto.getId()));
        //菜单表 in 菜单id
        List<SysMenuEntity> menuList = sysMenuService.list(new LambdaQueryWrapper<SysMenuEntity>()
                .in(SysMenuEntity::getId, roleMenuList.stream().map(SysRoleMenuEntity::getMenuId).collect(Collectors.toList()))
                .eq(SysMenuEntity::getStatus, SysMenuEntity.NORMAL_STATUS)
        );
        return RR.success("获取角色对应菜单成功", menuList);
    }


    @SysLog(logEnum = LogEnum.ADD_MENU)
    @PreAuthorize("hasAuthority('sys:menu:add')")
    @PostMapping(value = "/add", produces = BaseConsts.REQUEST_HEADERS_CONTENT_TYPE)
    @ApiOperation(value = "菜单管理-添加菜单", notes = "菜单管理-添加菜单", httpMethod = BaseConsts.REQUEST_METHOD, response = RR.class)
    public RR add(@Validated(Create.class) @RequestBody AddMenuDTO dto) throws Exception {
        if (sysMenuService.count(new LambdaQueryWrapper<SysMenuEntity>().eq(SysMenuEntity::getPerms, dto.getPerms())) > 0)
            throw new ServerException("授权标识已存在!");
        if (!dto.getParentId().equals(0)) {
            SysMenuEntity topMenu = sysMenuService.getById(dto.getParentId());
            if (topMenu == null) throw new ServerException("非法参数,查询父级菜单不存在");
            if (topMenu.getType().equals(SysMenuEntity.BUTTON_TYPE)) throw new ServerException("父级菜单不能是按钮");
        }
        SysMenuEntity data = new SysMenuEntity();
        BeanUtils.copyProperties(dto, data);
        Date date = new Date();
        sysMenuService.save(data.setId(null).setCreateTime(date).setUpdateTime(date));
        return RR.success("添加菜单成功");
    }


    @PreAuthorize("hasAuthority('sys:menu:add') || hasAuthority('sys:menu:update')")
    @PostMapping(value = "/topMenuTree", produces = BaseConsts.REQUEST_HEADERS_CONTENT_TYPE)
    @ApiOperation(value = "菜单管理-获取所有父级菜单树", notes = "菜单管理-获取所有父级菜单树", httpMethod = BaseConsts.REQUEST_METHOD, response = RR.class)
    public RR topMenuTree() {
        List<SysMenuEntity> topMenuList = sysMenuService.list(new LambdaQueryWrapper<SysMenuEntity>().eq(SysMenuEntity::getStatus, SysMenuEntity.NORMAL_STATUS).ne(SysMenuEntity::getType, SysMenuEntity.BUTTON_TYPE));
        topMenuList.add(new SysMenuEntity().setId(0L).setName("顶级菜单").setParentId(-1L));
        return RR.success("获取所有父级菜单树成功", TreeBuilder.buildMenu(topMenuList));
    }

    @SysLog(logEnum = LogEnum.EDIT_MENU)
    @PreAuthorize("hasAuthority('sys:menu:update')")
    @PostMapping(value = "/update", produces = BaseConsts.REQUEST_HEADERS_CONTENT_TYPE)
    @ApiOperation(value = "菜单管理-编辑菜单", notes = "菜单管理-编辑菜单", httpMethod = BaseConsts.REQUEST_METHOD, response = RR.class)
    public RR update(@Validated(Update.class) @RequestBody AddMenuDTO dto) throws Exception {
        SysMenuEntity menu = sysMenuService.getById(dto.getId());
        if (menu == null) throw new ServerException("非法ID,查询菜单信息不存在");
        //如果修改了父级菜单
        if (!dto.getParentId().equals(0) && !dto.getParentId().equals(menu.getParentId())) {
            SysMenuEntity topMenu = sysMenuService.getById(dto.getParentId());
            if (topMenu == null) throw new ServerException("非法参数,查询父级菜单不存在");
            if (topMenu.getType().equals(SysMenuEntity.BUTTON_TYPE)) throw new ServerException("父级菜单不能是按钮");
        }
        //如果修改菜单类型为按钮
        if (!menu.getType().equals(dto.getType())
                && dto.getType().equals(SysMenuEntity.BUTTON_TYPE)
                && sysMenuService.count(new LambdaQueryWrapper<SysMenuEntity>().eq(SysMenuEntity::getParentId, menu.getId())) > 0)
            throw new ServerException("该菜单存在下级菜单,无法将类型修改为按钮");
        //如果修改了授权标识
        if (!menu.getPerms().equals(dto.getPerms())) {
            SysMenuEntity one = sysMenuService.getOne(new LambdaQueryWrapper<SysMenuEntity>().eq(SysMenuEntity::getPerms, dto.getPerms()));
            if (one != null && one.getId() != dto.getId())
                throw new ServerException("授权标识已存在!");
        }

        menu.setParentId(dto.getParentId())
                .setName(dto.getName())
                .setUrl(dto.getUrl())
                .setPerms(dto.getPerms())
                .setType(dto.getType())
                .setIcon(dto.getIcon())
                .setOrderNum(dto.getOrderNum())
                .setStatus(dto.getStatus())
                .setUpdateTime(new Date());
        sysMenuService.updateById(menu);
        return RR.success("修改菜单成功");
    }


    @SysLog(logEnum = LogEnum.DELETE_MENU)
    @PreAuthorize("hasAuthority('sys:menu:delete')")
    @PostMapping(value = "/delete", produces = BaseConsts.REQUEST_HEADERS_CONTENT_TYPE)
    @ApiOperation(value = "菜单管理-删除菜单", notes = "菜单管理-删除菜单", httpMethod = BaseConsts.REQUEST_METHOD, response = RR.class)
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public RR delete(@Validated @RequestBody BaseIDTrueDTO dto) throws Exception {
        if (sysMenuService.count(new LambdaQueryWrapper<SysMenuEntity>().eq(SysMenuEntity::getParentId, dto.getId())) > 0)
            throw new ServerException("该菜单存在下级菜单,无法删除");
        sysMenuService.removeById(dto.getId());
        sysRoleMenuService.remove(new LambdaQueryWrapper<SysRoleMenuEntity>().eq(SysRoleMenuEntity::getMenuId, dto.getId()));
        return RR.success("删除菜单成功");
    }

}
