package com.tang.service;

import com.tang.base.dto.BaseIDTrueDTO;
import com.tang.dto.AddUserDTO;
import com.tang.dto.UserDTO;
import com.tang.entity.SysUserEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tang.utils.PageInfo;
import com.tang.vo.UserVO;
import com.tang.vo.excel.UserExport;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 后台用户表 服务类
 * </p>
 *
 * @author 芙蓉王
 * @since 2020-11-16
 */
public interface SysUserService extends IService<SysUserEntity> {


    /**
     * 功能描述: 分页查询用户列表
     *
     * @创建人: 芙蓉王
     * @创建时间: 2020年11月18日 16:44:00
     * @param dto
     * @param deptId  如果 == -1 则可以查询所有部门  如果是当前登录用户非管理员 则只能查询自己所属部门下的用户
     * @return: com.tang.utils.PageInfo
     **/
    PageInfo<UserVO> pageInfo(UserDTO dto, Long deptId);

    /**
     * 功能描述: 根据id查询用户信息
     *
     * @创建人: 芙蓉王
     * @创建时间: 2020年11月20日 16:50:01
     * @param userId
     * @param deptId  如果 == -1 则可以查询所有部门  如果是当前登录用户非管理员 则只能查询自己所属部门下的用户
     * @return: com.tang.vo.UserVO
     **/
    UserVO getUserInfoById(Long userId, Long deptId);

    /**
     * 功能描述: 导出用户列表
     *
     * @创建人: 芙蓉王
     * @创建时间: 2020年11月21日 10:57:55
     * @param dto
     * @param deptId
     * @return: java.util.List<com.tang.vo.excel.UserExport>
     **/
    List<UserExport> exportList(UserDTO dto, Long deptId);
}
