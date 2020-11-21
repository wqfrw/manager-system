package com.tang.base.factory;

import com.tang.base.exception.RequestDataException;
import org.springframework.validation.BindingResult;

/**
 * 
 * @ClassName ValidateHandle
 * @Description 校验请求数据
 * @author 芙蓉王
 * @Date Apr 6, 2020 3:29:48 PM
 * @version 1.0.0
 */
public class ValidateHandle {

    /**
     * 
     * @Description 校验请求数据
     * @param error
     * @throws Exception
     */
    public static void validReqData(BindingResult error) throws Exception{
        if(error.hasErrors()) {
            throw new RequestDataException(error.getFieldError().getDefaultMessage());
        }
    }
    
    
//    /**
//     *
//     * @Description 校验包含agentId的请求数据
//     * @param error
//     * @throws Exception
//     */
//    public static void validReqDataContainsCid(BindingResult error) throws Exception{
//        if(error.hasErrors()) {
//            throw new RequestDataException(error.getFieldError().getDefaultMessage());
//        }
//        Integer cid = Integer.parseInt(String.valueOf(error.getFieldValue("cid")));
//        boolean isAdmin = SecurityAuthorHolder.getSecurityUser().getUserType().equals(SysUser.SUPER_USER_TYPE);
//        if(!isAdmin && !cid.equals(SecurityAuthorHolder.getSecurityUser().getCid())) throw new UnauthorException();
//    }
    
//    /**
//     *
//     * @Description 校验包含平台商编码的请求数据
//     * @param error
//     * @throws Exception
//     */
//    public static void validReqDataContainsCagent(BindingResult error) throws Exception{
//        if(error.hasErrors()) {
//            throw new RequestDataException(error.getFieldError().getDefaultMessage());
//        }
//        String cagent = String.valueOf(error.getFieldValue("cagent"));
//        boolean isAdmin = SecurityAuthorHolder.getSecurityUser().getUserType().equals(SysUser.SUPER_USER_TYPE);
//        if(!isAdmin && !cagent.equalsIgnoreCase(SecurityAuthorHolder.getSecurityUser().getCagent())) throw new UnauthorException();
//    }
    
}

