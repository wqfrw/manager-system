package com.tang.controller.sys;


import cn.hutool.crypto.digest.MD5;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tang.base.annotation.SysLog;
import com.tang.base.consts.BaseConsts;
import com.tang.base.dto.BaseIDTrueDTO;
import com.tang.base.dto.RR;
import com.tang.base.enums.LogEnum;
import com.tang.base.exception.ServerException;
import com.tang.base.factory.ValidateHandle;
import com.tang.base.security.SecurityUser;
import com.tang.base.validator.Create;
import com.tang.base.validator.Update;
import com.tang.controller.BaseController;
import com.tang.dto.AddUserDTO;
import com.tang.dto.EditPasswordDTO;
import com.tang.dto.UserDTO;
import com.tang.dto.excel.UserImport;
import com.tang.entity.SysRoleEntity;
import com.tang.entity.SysUserEntity;
import com.tang.entity.SysUserRoleEntity;
import com.tang.service.SysRoleService;
import com.tang.service.SysUserRoleService;
import com.tang.service.SysUserService;
import com.tang.utils.ExportExcel;
import com.tang.utils.ImportExcel;
import com.tang.utils.PageInfo;
import com.tang.vo.UserVO;
import com.tang.vo.excel.UserExport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 后台用户表 前端控制器
 * </p>
 *
 * @author 芙蓉王
 * @since 2020-11-16
 */
@RestController
@RequestMapping("/sys/user")
@Api(value = "系统管理-用户管理接口", tags = "系统管理-用户管理接口")
public class SysUserController extends BaseController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysUserRoleService sysUserRoleService;

    @PreAuthorize("hasAuthority('sys:user:page')")
    @PostMapping(value = "/page", produces = BaseConsts.REQUEST_HEADERS_CONTENT_TYPE)
    @ApiOperation(value = "用户管理-用户列表", notes = "用户管理-用户列表", httpMethod = BaseConsts.REQUEST_METHOD, response = RR.class)
    public RR page(@Validated @RequestBody UserDTO dto) {
        PageInfo<UserVO> pageInfo = sysUserService.pageInfo(dto, isAdmin() ? -1 : getSecurityUser().getDept().getId());
        return RR.success("分页查询用户列表成功", pageInfo);
    }

    @SysLog(logEnum = LogEnum.ADD_USER, filterParams = {"password"})
    @PreAuthorize("hasAuthority('sys:user:add')")
    @PostMapping(value = "/add", produces = BaseConsts.REQUEST_HEADERS_CONTENT_TYPE)
    @ApiOperation(value = "用户管理-添加用户", notes = "用户管理-添加用户", httpMethod = BaseConsts.REQUEST_METHOD, response = RR.class)
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public RR add(@Validated(Create.class) @RequestBody AddUserDTO dto, BindingResult error) throws Exception {
        //校验数据  DTO中有正则的话需要用此方法校验参数
        ValidateHandle.validReqData(error);
        SysUserEntity one = sysUserService.getOne(new LambdaQueryWrapper<SysUserEntity>().eq(SysUserEntity::getLoginName, dto.getLoginName()));
        if (one != null) throw new ServerException("该用户名已经被别人注册啦!");

        SysRoleEntity role = sysRoleService.getById(dto.getRoleId());
        if (role == null) throw new ServerException("非法角色id,查询角色信息失败");
        //管理员则可以添加任意角色,反之只能为登录用户所属部门下的角色
        if (!isAdmin() && !getSecurityUser().getDept().getId().equals(role.getDeptId()))
            throw new ServerException("非法角色id,该角色不属于当前登录用户所属部门");

        //生成密码盐
        String salt = UUID.randomUUID().toString();
        //密码加密
        String password = new MD5(salt.getBytes()).digestHex(dto.getPassword().getBytes());
        Date date = new Date();

        SysUserEntity data = new SysUserEntity();
        BeanUtils.copyProperties(dto, data);

        data.setId(null)
                .setCreateTime(date)
                .setPassword(password)
                .setSalt(salt)
                .setLoginstatus(0);

        sysUserService.save(data);
        sysUserRoleService.save(new SysUserRoleEntity().setUserId(data.getId()).setRoleId(role.getId()).setCreateTime(date).setUpdateTime(date));
        return RR.success("新增用户信息成功");
    }


    @SysLog(logEnum = LogEnum.EDIT_PWD, filterParams = {"pwd", "newPwd"})
    @PreAuthorize("hasAuthority('sys:user:editPwd')")
    @PostMapping(value = "/editPwd", produces = BaseConsts.REQUEST_HEADERS_CONTENT_TYPE)
    @ApiOperation(value = "用户管理-修改登录密码", notes = "用户管理-修改登录密码", httpMethod = BaseConsts.REQUEST_METHOD, response = RR.class)
    public RR editPwd(@Validated @RequestBody EditPasswordDTO dto) throws Exception {
        SecurityUser securityUser = getSecurityUser();
        String password = new MD5(securityUser.getSalt().getBytes()).digestHex(dto.getPwd().getBytes());
        if (!securityUser.getPassword().equals(password)) throw new ServerException("原密码输入不正确");

        //密码加密
        String newPwd = new MD5(securityUser.getSalt().getBytes()).digestHex(dto.getNewPwd().getBytes());
        sysUserService.updateById(new SysUserEntity().setId(securityUser.getId()).setPassword(newPwd));
        loginOut();
        return RR.success("修改密码成功,请重新登录");
    }

    @PreAuthorize("hasAuthority('sys:user:update')")
    @PostMapping(value = "/info", produces = BaseConsts.REQUEST_HEADERS_CONTENT_TYPE)
    @ApiOperation(value = "用户管理-查询用户信息", notes = "用户管理-查询用户信息", httpMethod = BaseConsts.REQUEST_METHOD, response = RR.class)
    public RR page(@Validated @RequestBody BaseIDTrueDTO dto) {
        UserVO userVO = sysUserService.getUserInfoById(dto.getId(), isAdmin() ? -1 : getSecurityUser().getDept().getId());
        return RR.success("查询用户信息成功", userVO);
    }

    @SysLog(logEnum = LogEnum.EDIT_USER, filterParams = {"password"})
    @PreAuthorize("hasAuthority('sys:user:update')")
    @PostMapping(value = "/update", produces = BaseConsts.REQUEST_HEADERS_CONTENT_TYPE)
    @ApiOperation(value = "用户管理-修改用户", notes = "用户管理-修改用户", httpMethod = BaseConsts.REQUEST_METHOD, response = RR.class)
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public RR update(@Validated(Update.class) @RequestBody AddUserDTO dto, BindingResult error) throws Exception {
        //校验请求数据
        ValidateHandle.validReqData(error);

        if (dto.getId().equals(getUserId())) throw new ServerException("非法参数,无法修改自身");

        SysUserEntity user = sysUserService.getById(dto.getId());
        if (user == null) throw new ServerException("非法用户id,查询用户信息失败");

        SysRoleEntity role = sysRoleService.getById(dto.getRoleId());
        if (role == null) throw new ServerException("非法角色id,查询角色信息失败");

        //管理员则可以修改为任意角色,反之只能为登录用户所属部门下的角色
        if (!isAdmin() && !getSecurityUser().getDept().getId().equals(role.getDeptId()))
            throw new ServerException("非法角色id,该角色不属于当前登录用户所属部门");


        sysUserService.updateById(new SysUserEntity().setId(dto.getId()).setName(dto.getName()).setSex(dto.getSex()).setAge(dto.getAge()).setStatus(dto.getStatus()));
        sysUserRoleService.update(new SysUserRoleEntity().setRoleId(dto.getRoleId()), new LambdaQueryWrapper<SysUserRoleEntity>().eq(SysUserRoleEntity::getUserId, dto.getId()));
        loginOut(user.getLoginName());
        return RR.success("更新用户信息成功,为了数据一致,该用户需重新登录");
    }


    @SysLog(logEnum = LogEnum.DELETE_USER)
    @PreAuthorize("hasAuthority('sys:user:delete')")
    @PostMapping(value = "/delete", produces = BaseConsts.REQUEST_HEADERS_CONTENT_TYPE)
    @ApiOperation(value = "用户管理-删除用户", notes = "用户管理-删除用户", httpMethod = BaseConsts.REQUEST_METHOD, response = RR.class)
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public RR delete(@Validated @RequestBody BaseIDTrueDTO dto) throws Exception {
        if (dto.getId().equals(getUserId())) throw new ServerException("非法参数,无法删除自身");

        SysUserEntity user = sysUserService.getById(dto.getId());
        if (user == null) throw new ServerException("非法用户id,查询用户信息失败");

        //如果非管理员则只能删除自己所属部门下的用户的信息
        if (!isAdmin()) {
            SysUserRoleEntity userRoleEntity = sysUserRoleService.getOne(new LambdaQueryWrapper<SysUserRoleEntity>().eq(SysUserRoleEntity::getUserId, dto.getId()));
            SysRoleEntity role = sysRoleService.getById(userRoleEntity.getRoleId());
            if (!getSecurityUser().getDept().getId().equals(role.getDeptId()))
                throw new ServerException("非法用户id,该用户非当前登录用户所属部门");
        }
        sysUserService.removeById(dto.getId());
        sysUserRoleService.remove(new LambdaQueryWrapper<SysUserRoleEntity>().eq(SysUserRoleEntity::getUserId, dto.getId()));
        loginOut(user.getLoginName());
        return RR.success("删除用户信息成功");
    }


    @PreAuthorize("hasAuthority('sys:user:add')")
    @PostMapping(value = "/importUser", produces = BaseConsts.REQUEST_HEADERS_CONTENT_TYPE)
    @ApiOperation(value = "用户管理-导入用户", notes = "用户管理-导入用户", httpMethod = BaseConsts.REQUEST_METHOD, response = RR.class)
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public RR importUser(@RequestParam(value = "file") MultipartFile file, HttpServletRequest request) throws Exception {
        List<UserImport> dataList = new ImportExcel(file, 1, 0).getDataList(UserImport.class);
        dataList.forEach(System.err::println);
        return RR.success("导入用户信息接口正在开发中...");
    }

    @PreAuthorize("hasAuthority('sys:user:page')")
    @PostMapping(value = "/export", produces = BaseConsts.REQUEST_HEADERS_CONTENT_TYPE)
    @ApiOperation(value = "用户管理-导出用户", notes = "用户管理-导出用户", httpMethod = BaseConsts.REQUEST_METHOD, response = RR.class)
    public void export(@Validated @RequestBody UserDTO dto, HttpServletResponse response) throws Exception {
        MultipartFile multipartFile = null;
        new ImportExcel(multipartFile, 1, 1).getDataList(UserDTO.class);

        List<UserExport> exportList = sysUserService.exportList(dto, isAdmin() ? -1 : getSecurityUser().getDept().getId());
        new ExportExcel("用户列表Excel文档", UserExport.class, 2)
                .setDataList(exportList)
                .write(response, "用户列表Excel文档.xlsx")
                .dispose();
    }
}
