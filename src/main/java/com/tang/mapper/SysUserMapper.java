package com.tang.mapper;

import com.tang.dto.UserDTO;
import com.tang.entity.SysUserEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tang.utils.PageInfo;
import com.tang.vo.UserVO;
import com.tang.vo.excel.UserExport;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 后台用户表 Mapper 接口
 * </p>
 *
 * @author 芙蓉王
 * @since 2020-11-16
 */
public interface SysUserMapper extends BaseMapper<SysUserEntity> {

    /**
     * 功能描述: 查询用户列表
     *
     * @param pageInfo
     * @param deptId
     * @创建人: 芙蓉王
     * @创建时间: 2020年11月18日 16:47:34
     * @return: java.util.List<com.tang.vo.UserVO>
     **/
    List<UserVO> pageInfo(@Param("pageInfo") PageInfo<UserVO> pageInfo, @Param("deptId") Long deptId);

    /**
     * 功能描述: 查询用户列表总条数
     *
     * @param pageInfo
     * @param deptId
     * @创建人: 芙蓉王
     * @创建时间: 2020年11月20日 15:34:48
     * @return: java.lang.Integer
     **/
    Integer pageInfoTotal(@Param("pageInfo") PageInfo<UserVO> pageInfo, @Param("deptId") Long deptId);

    /**
     * 功能描述: 根据id查询用户信息
     *
     * @param userId
     * @param deptId
     * @创建人: 芙蓉王
     * @创建时间: 2020年11月20日 16:52:10
     * @return: com.tang.vo.UserVO
     **/
    UserVO getUserInfoById(@Param("userId") Long userId, @Param("deptId") Long deptId);

    /**
     * 功能描述: 导出用户列表
     *
     * @param dto
     * @param deptId
     * @创建人: 芙蓉王
     * @创建时间: 2020年11月21日 10:58:39
     * @return: java.util.List<com.tang.vo.excel.UserExport>
     **/
    List<UserExport> exportList(@Param("dto") UserDTO dto, @Param("deptId") Long deptId);
}
