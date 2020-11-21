package com.tang.mapper;

import com.tang.entity.SysRoleEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @author 芙蓉王
 * @since 2020-11-16
 */
public interface SysRoleMapper extends BaseMapper<SysRoleEntity> {

    /**
     * 功能描述: 根据userId查询对应角色名
     *
     * @创建人: 芙蓉王
     * @创建时间: 2020年11月16日 19:17:17
     * @param userId
     * @return: java.lang.String
     **/
    SysRoleEntity selectByUserId(Long userId);
}
