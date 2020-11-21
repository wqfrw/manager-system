package com.tang.service.impl;

import com.tang.dto.UserDTO;
import com.tang.entity.SysUserEntity;
import com.tang.mapper.SysUserMapper;
import com.tang.service.SysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tang.utils.PageInfo;
import com.tang.vo.UserVO;
import com.tang.vo.excel.UserExport;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * <p>
 * 后台用户表 服务实现类
 * </p>
 *
 * @author 芙蓉王
 * @since 2020-11-16
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUserEntity> implements SysUserService {

    @Override
    public PageInfo pageInfo(UserDTO dto, Long deptId) {
        PageInfo<UserVO> pageInfo = new PageInfo(dto);
        List<UserVO> userList = baseMapper.pageInfo(pageInfo, deptId);
        if (!CollectionUtils.isEmpty(userList)) {
            pageInfo.setRecords(userList);
            pageInfo.setTotal(baseMapper.pageInfoTotal(pageInfo, deptId));
        }
        return pageInfo;
    }

    @Override
    public UserVO getUserInfoById(Long userId, Long deptId) {
        return baseMapper.getUserInfoById(userId, deptId);
    }

    @Override
    public List<UserExport> exportList(UserDTO dto, Long deptId) {
        return baseMapper.exportList(dto, deptId);
    }
}
