package com.tang.base.aspect;

import com.alibaba.fastjson.JSONObject;
import com.tang.entity.SysLogEntity;
import com.tang.base.security.SecurityAuthorHolder;
import com.tang.base.security.SecurityUser;
import com.tang.service.SysLogService;
import com.tang.utils.ServletRequestUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;

/**
 * 功能描述: 系统日志,切面处理类
 *
 * @param
 * @创建人: 芙蓉王
 * @return:
 **/
@Aspect
@Component
public class SysLogAspect {

    @Autowired
    private SysLogService sysLogService;


    @Pointcut("@annotation(com.tang.base.annotation.SysLog)")
    public void logPointCut() {

    }

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        com.tang.base.annotation.SysLog syslog = method.getAnnotation(com.tang.base.annotation.SysLog.class);
        //日志记录类
        SysLogEntity param = new SysLogEntity();
        if (syslog != null) {
            //注解上的描述
            param.setMenu(syslog.logEnum().getMenu());
            param.setFunction(syslog.logEnum().getFunction());
        }
        Object proceed;
        try {
            //请求的参数
            Object[] args = joinPoint.getArgs();
            String paramJsonStr = JSONObject.toJSONString(args[0]);
            JSONObject paramJson = JSONObject.parseObject(paramJsonStr);

            //执行方法 拿到返回结果
            proceed = joinPoint.proceed();
            if (!ObjectUtils.isEmpty(proceed)) {
                //把返回结果解析成json串
                String resultJsonStr = JSONObject.toJSONString(proceed);
                JSONObject resultJson = JSONObject.parseObject(resultJsonStr);
                //返回参数中的data对象
                JSONObject data = resultJson.getJSONObject("data");

                //过滤指定参数 将指定参数的value设置为 ******
                Arrays.stream(syslog.filterParams()).forEach(v -> {
                    if (paramJson != null && paramJson.containsKey(v)) {
                        paramJson.put(v, "******");
                    }

                    if (data != null && data.containsKey(v)) {
                        data.put(v, "******");
                    }
                });
                param.setParameters(paramJson.toJSONString());
                param.setResult(resultJson.toJSONString());
            }
        } catch (Throwable throwable) {
            //如果方法抛出异常则直接抛出异常  不记录日志
            throw throwable;
        }
        //获取request
        HttpServletRequest request = ServletRequestUtil.getHttpServletRequest();
        //设置IP地址
        param.setIp(ServletRequestUtil.getIp(request));
        //操作人
        SecurityUser user = null;
        try {
            user = SecurityAuthorHolder.getSecurityUser();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (user != null) {
            param.setCreater(user.getLoginName());
        }
        //操作时间
        param.setCreateTime(new Date());
        //保存系统日志
        sysLogService.save(param);
        return proceed;
    }
}
