package com.tang.base.factory;


/**
 * 
 * @ClassName CommonDataFactory
 * @Description 公共数据工厂类
 * @author 芙蓉王
 * @Date Apr 7, 2020 12:29:13 PM
 * @version 1.0.0
 */
public class CommonDataFactory {


//    private static SysMenuMapper sysMenuMapper;
//
//    static {
//        sysMenuMapper = ApplicationConfig.getBean("sysMenuMapper",SysMenuMapper.class);
//    }
//
//    /**
//     *
//     * @Description 获取系统菜单列表
//     * @return
//     */
//    public static List<SysMenu> getSysMenuList(){
//        String cache_menu_key = "ADMIN:SYS:MENUS";
//        if(CollectionUtils.isEmpty(LocalCacheConsts.menumaps)) {
//            List<SysMenu> menus = sysMenuMapper.selectList(null);
//            if(!CollectionUtils.isEmpty(menus)) {
//                LocalCacheConsts.menumaps.put(cache_menu_key, menus);
//            }
//        }
//        return LocalCacheConsts.menumaps.get(cache_menu_key);
//    }
//
//    /**
//     *
//     * @Description 清空菜单缓存
//     */
//    public static void clearMenu() {
//        LocalCacheConsts.menumaps.clear();
//    }
}
