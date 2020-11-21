package com.tang.service.impl;

import com.tang.entity.AccountEntity;
import com.tang.mapper.AccountMapper;
import com.tang.service.AccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 账号表 服务实现类
 * </p>
 *
 * @author 芙蓉王
 * @since 2020-11-16
 */
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, AccountEntity> implements AccountService {

}
