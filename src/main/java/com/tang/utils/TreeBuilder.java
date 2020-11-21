package com.tang.utils;

import com.tang.entity.SysMenuEntity;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Peter
 * @version 1.0.0
 * @ClassName MenuTreeBuilder
 * @Description 菜单树工具类
 * @Date Apr 7, 2020 11:07:10 AM
 */
public class TreeBuilder {

    /**
     * 功能描述: 构建菜单树
     *
     * @创建人: Peter
     * @创建时间: 2020年06月12日 19:52:34
     * @param menus
     * @return: java.util.List<com.cn.tianxia.admin.entity.TxhMenu>
     **/
    public static List<SysMenuEntity> buildMenu(List<SysMenuEntity> menus) {
        //过滤所有目录
        List<SysMenuEntity> directoryList = menus.stream().filter(v -> SysMenuEntity.DIRECTORY_TYPE.equals(v.getType())).collect(Collectors.toList());
        directoryList.stream().forEach(v -> findChildNode(v,menus));
        return directoryList;
    }

    /**
     * 功能描述: 递归查找子节点
     *
     * @创建人: Peter
     * @创建时间: 2020年06月12日 19:52:19
     * @param
     * @return: com.cn.tianxia.admin.entity.TxhMenu
     **/
    public static SysMenuEntity findChildNode(SysMenuEntity root,List<SysMenuEntity> menus){
        List<SysMenuEntity> nodes = menus.stream().filter(v -> v.getParentId().equals(root.getId())).collect(Collectors.toList());
        nodes.stream().forEach(v -> findChildNode(v,menus));
        nodes.stream().sorted(Comparator.comparing(SysMenuEntity::getOrderNum));
        root.setMenuTree(nodes);
        return root;
    }

}
