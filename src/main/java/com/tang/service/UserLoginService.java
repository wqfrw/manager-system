package com.tang.service;

import com.tang.dto.LoginDTO;
import com.tang.vo.UserVO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserLoginService extends UserDetailsService {

    /**
     * 功能描述: 用户登录
     *
     * @创建人: 芙蓉王
     * @创建时间: 2020年11月16日 19:26:59
     * @param loginDTO
     * @return: com.tang.vo.UserVO
     **/
    UserVO login(LoginDTO loginDTO);
}
