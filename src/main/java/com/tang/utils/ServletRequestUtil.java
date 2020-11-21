package com.tang.utils;

import com.tang.base.filter.XssHttpServletRequestWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * 
 * @ClassName ServletRequestUtil
 * @Description ServletRequest工具类
 * @author 芙蓉王
 * @Date Mar 7, 2020 11:18:00 AM
 * @version 1.0.0
 */
public class ServletRequestUtil {
    
    /**
     * IP文件解析参数
     */
    private static long max=(1L << 32) - 1;
    
    /**
     * 
     * @Description 读取request 已经被防止XSS，SQL注入过滤过的IP
     * @param request
     * @return
     * @throws Exception
     */
    public static String getIp(HttpServletRequest request)throws RuntimeException{
        String ipAddr= null;
        try {
            ipAddr = null;
            String ipString = ServletRequestUtil.getHeader(request,"X-Real-IP");
            if (StringUtils.isEmpty(ipString) || "unKnown".equalsIgnoreCase(ipString)) {
                ipString = ServletRequestUtil.getHeader(request,"X-Forwarded-For");
                if (StringUtils.isNotEmpty(ipString) && !"unKnown".equalsIgnoreCase(ipString)) ipAddr=ipString;
                ipAddr=request.getRemoteAddr();
            }else{
                 int index = ipString.indexOf(",");
                 if (-1==index) {
                     ipAddr=ipString;
                 }else{
                     ipAddr=ipString.substring(0, index);
                 }
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return StringUtils.isBlank(ipAddr) || ipAddr.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1":ipAddr;
    }

    /**
     * 获取客户机ip地址
     *
     * @param request:
     * @return: java.lang.String
     */
    public static String getIpAdrress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("http_client_ip");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        // 如果是多级代理，那么取第一个ip为客户ip
        if (ip != null && ip.indexOf(",") != -1) {
            ip = ip.substring(ip.lastIndexOf(",") + 1, ip.length()).trim();
        }
        return ip;
    }
    
    /**
     * 
     * @Description 读取request 已经被防止XSS，SQL注入过滤过的IP
     * @param request
     * @return
     * @throws Exception
     */
    public static String getIp(ServletRequest request)throws RuntimeException{
        String ipAddr=null;
        String ipString = ServletRequestUtil.getHeader(request,"X-Forwarded-For");
        if (StringUtils.isEmpty(ipString) || "unKnown".equalsIgnoreCase(ipString)) {
            ipString = ServletRequestUtil.getHeader(request,"X-Real-IP");
            if (StringUtils.isNotEmpty(ipString) && !"unKnown".equalsIgnoreCase(ipString)) ipAddr=ipString;
            ipAddr=request.getRemoteAddr();
        }else{
             int index = ipString.indexOf(",");
             if (-1==index) {
                 ipAddr=ipString;
             }else{
                 ipAddr=ipString.substring(0, index);
             }
        }
        return StringUtils.isBlank(ipAddr) || ipAddr.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1":ipAddr;
    }
    
    /**
     * 获取Linux下的IP地址
     *
     * @return IP地址
     * @throws SocketException
     */
    public static String getLinuxServerIp() throws SocketException {
        String ip = "";
        for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
            NetworkInterface intf = en.nextElement();
            String name = intf.getName();
            if (!name.contains("docker") && !name.contains("lo")) {
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String ipaddress = inetAddress.getHostAddress().toString();
                        if (!ipaddress.contains("::") && !ipaddress.contains("0:0:") && !ipaddress.contains("fe80")) {
                            ip = ipaddress;
                        }
                    }
                }
            }
        }
        return StringUtils.isBlank(ip)?"127.0.0.1":ip;
    }
    
    /**
     * 
     * @Description 读取request 已经被防止XSS，SQL注入过滤过的 请求头key 对应的value
     * @param request
     * @param key
     * @return
     */
    public static String getHeader(HttpServletRequest request, String key) throws RuntimeException{
        return ServletRequestUtil.getRequest(request).getHeader(key);
    }
    
    
    /**
     * 
     * @Description 读取request 已经被防止XSS，SQL注入过滤过的 请求头key 对应的value
     * @param request
     * @param key
     * @return
     */
    public static String getHeader(ServletRequest request, String key) throws RuntimeException{
        return ServletRequestUtil.getRequest(request).getHeader(key);
    }

    
    /**
     * 
     * @Description 获取已经被防止XSS，SQL注入过滤过的request
     * @param request
     * @return
     */
    public static HttpServletRequest getRequest(ServletRequest request) throws RuntimeException{
        return new XssHttpServletRequestWrapper((HttpServletRequest) request);
    }
    
    
    /**
     * 将IP地址转换为长整型
     * @param strIp IP地址
     * @return 长整型
     */
    public static long ipToLong(String strIp) throws RuntimeException{
        long[] ip = new long[4];
        int position1 = strIp.indexOf(".");
        int position2 = strIp.indexOf(".", position1 + 1);
        int position3 = strIp.indexOf(".", position2 + 1);
        ip[0] = Long.parseLong(strIp.substring(0, position1));
        ip[1] = Long.parseLong(strIp.substring(position1 + 1, position2));
        ip[2] = Long.parseLong(strIp.substring(position2 + 1, position3));
        ip[3] = Long.parseLong(strIp.substring(position3 + 1));
        return ((ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3]) & max;
    }
    
    /**
     * 
     * @Description 获取HttpServletRequest
     * @return
     */
    public static HttpServletRequest getHttpServletRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }
    
    /**
     * 
     * @Description 获取域名
     * @return
     */
    public static String getDomain(){
        HttpServletRequest request = getHttpServletRequest();
        StringBuffer url = request.getRequestURL();
        return url.delete(url.length() - request.getRequestURI().length(), url.length()).toString();
    }

    public static String getOrigin(){
        HttpServletRequest request = getHttpServletRequest();
        return request.getHeader("Origin");
    }
  
}
