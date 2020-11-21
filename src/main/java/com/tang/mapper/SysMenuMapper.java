package com.tang.mapper;

import com.tang.entity.SysMenuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 菜单管理表 Mapper 接口
 * </p>
 *
 * @author 芙蓉王
 * @since 2020-11-16
 */
public interface SysMenuMapper extends BaseMapper<SysMenuEntity> {

    /**
     * 功能描述: 根据roleId查询对应的权限集合
     *
     * @创建人: 芙蓉王
     * @创建时间: 2020年11月16日 19:22:11
     * @param roleId
     * @return: java.util.List<com.tang.entity.SysMenuEntity>
     **/
    List<SysMenuEntity> selectByRoleId(Long roleId);
}
