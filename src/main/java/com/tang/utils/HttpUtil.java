package com.tang.utils;


import com.alibaba.fastjson.JSONObject;
import com.tang.base.dto.HR;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.util.CollectionUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author
 * @version 1.0.0
 * @ClassName HttpUtil
 * @Description httpclient 工具类
 * @Date Dec 28, 2019 9:22:25 PM
 */
public class HttpUtil {

    public static String sendGet(String url, String param) {
        StringBuilder sb = new StringBuilder();
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty(URLEncoder.encode(":authority", "UTF-8"), "otc-api-hk.eiijo.cn");
            connection.setRequestProperty(URLEncoder.encode(":method", "UTF-8"), "GET");
            connection.setRequestProperty(URLEncoder.encode(":path", "UTF-8"), "/v1/data/trade-market?coinId=2&currency=1&tradeType=sell&currPage=1&payMethod=2&country=37&blockType=general&online=1&range=0&amount=");
            connection.setRequestProperty(URLEncoder.encode(":scheme", "UTF-8"), "https");
            connection.setRequestProperty("accept", "application/json, text/plain, */*");
            connection.setRequestProperty("accept-encoding", "gzip, deflate, br");
            connection.setRequestProperty("accept-language", "zh-CN,zh;q=0.9");
            connection.setRequestProperty("client-type", "web");
            connection.setRequestProperty("fingerprint", "6422629d8746529af3c59f0e74cce98b");
            connection.setRequestProperty("origin", "https://c2c.huobi.vc");
            connection.setRequestProperty("otc-language", "zh-CN");
            connection.setRequestProperty("portal", "web");
            connection.setRequestProperty("referer", "https://c2c.huobi.vc/");
            connection.setRequestProperty("sec-fetch-dest", "empty");
            connection.setRequestProperty("sec-fetch-mode", "cors");
            connection.setRequestProperty("sec-fetch-site", "cross-site");
            connection.setRequestProperty("token", "undefined");
            connection.setRequestProperty("trace_id", "4b1e2805-31f4-417f-966f-670e88fddb4e");
            connection.setRequestProperty("uid", "0");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.setRequestProperty("x-requested-with", "XMLHttpRequest");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return sb.toString();
    }

    public static HR sendPost(String url, Map<String,String> data) throws Exception {
        CloseableHttpClient httpclient = null;
        String result = null;
        try {
            httpclient = HttpClients.custom()
                    .setConnectionManager(createConnectionManager())
                    .build();
            HttpPost httppost = new HttpPost(url);
            httppost.setHeader("Accept", "text/javascript, application/javascript, application/ecmascript, application/x-ecmascript, */*; q=0.01");
            httppost.setHeader("Accept-Encoding", "gzip, deflate");
            httppost.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
            httppost.setHeader("Connection", "keep-alive");
            httppost.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            httppost.setHeader("Cookie", "Hm_lvt_4832a4717bd0c39cf6d1c1645fe420cb=1603882391; Hm_lpvt_4832a4717bd0c39cf6d1c1645fe420cb=1603882391");
            httppost.setHeader("Host", "gequdaquan.net");
            httppost.setHeader("Origin", "http://gequdaquan.net");
            httppost.setHeader("Referer", "http://gequdaquan.net/gqss/");
            httppost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.111 Safari/537.36");
            httppost.setHeader("X-Requested-With", "XMLHttpRequest");
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            // 设置包体参数。
            if (!CollectionUtils.isEmpty(data)) {
                data.keySet().forEach(e -> {
                    NameValuePair nvp = new BasicNameValuePair(e, data.get(e));
                    nvps.add(nvp);
                });
            }
            StringEntity se = new UrlEncodedFormEntity(nvps, Consts.UTF_8);
            httppost.setEntity(se);
            httppost.setConfig(getConfig());
            CloseableHttpResponse response = httpclient.execute(httppost);

            HttpEntity entity = response.getEntity();
            BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), Consts.UTF_8));
            StringBuffer sb = new StringBuffer();
            String content = null;
            while ((content = reader.readLine()) != null) {
                sb.append(content);
            }
            result = sb.toString();

            return new HR(response.getStatusLine().getStatusCode(), result);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (httpclient != null) {
                httpclient.close();
            }
        }
    }

    /**
     * @param url url+参数组合的请求地址
     * @return
     * @throws Exception
     * @Description Content-Type:"application/json"类型的get请求
     */
    public static HR toGetJson(String url) throws Exception {
        CloseableHttpClient httpclient = null;
        String result = null;
        try {
            httpclient = HttpClients.custom()
                    .setConnectionManager(createConnectionManager())
                    .build();
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("Content-Type", "application/json");
            httpGet.setHeader("charset", Consts.UTF_8.toString());
            httpGet.setConfig(getConfig());
            CloseableHttpResponse response = httpclient.execute(httpGet);

            HttpEntity entity = response.getEntity();
            BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), Consts.UTF_8));
            StringBuffer sb = new StringBuffer();
            String content = null;
            while ((content = reader.readLine()) != null) {
                sb.append(content);
            }
            result = sb.toString();

            return new HR(response.getStatusLine().getStatusCode(), result);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (httpclient != null) {
                httpclient.close();
            }
        }
    }

    /**
     * @param url
     * @param data
     * @return
     * @throws Exception
     * @Description Content-Type:"application/json"类型的get请求
     */
    public static HR toGetJson(String url, Map<String, String> data) throws Exception {
        CloseableHttpClient httpclient = null;
        String result = null;
        try {
            httpclient = HttpClients.custom()
                    .setConnectionManager(createConnectionManager())
                    .build();
            StringBuffer buf = new StringBuffer();
            //拼装请求参数
            if (!CollectionUtils.isEmpty(data)) {
                data.keySet().stream().forEach(key -> {
                    buf.append("&").append(key).append("=").append(data.get(key));
                });
                ;
            }
            String params = buf.toString().replaceFirst("&", "");
            url = url.contains("?") ? url + params : url + "?" + params;
            //初始化get请求函数
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("Content-Type", "application/json");
            httpGet.setHeader("charset", Consts.UTF_8.toString());
            httpGet.setConfig(getConfig());
            CloseableHttpResponse response = httpclient.execute(httpGet);

            HttpEntity entity = response.getEntity();
            BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), Consts.UTF_8));
            StringBuffer sb = new StringBuffer();
            String content = null;
            while ((content = reader.readLine()) != null) {
                sb.append(content);
            }
            result = sb.toString();

            return new HR(response.getStatusLine().getStatusCode(), result);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (httpclient != null) {
                httpclient.close();
            }
        }
    }


    /**
     * @param url
     * @param url
     * @return
     * @throws Exception
     * @Description Content-Type:"application/json"类型的get请求
     */
    public static HR toGetJsonContainsHeader(String url, Map<String, String> header) throws Exception {
        CloseableHttpClient httpclient = null;
        String result = null;
        try {
            httpclient = HttpClients.custom()
                    .setConnectionManager(createConnectionManager())
                    .build();
            StringBuffer buf = new StringBuffer();
            String params = buf.toString().replaceFirst("&", "");
            url = url.contains("?") ? url + params : url + "?" + params;
            //初始化get请求函数
            HttpGet httpGet = new HttpGet(url);

            //设置请求头
            if (!CollectionUtils.isEmpty(header)) {
                header.keySet().stream().forEach(key -> {
                    httpGet.setHeader(key, header.get(key));
                });
            }
            httpGet.setHeader("Content-Type", "application/json");
            httpGet.setHeader("charset", Consts.UTF_8.toString());
            httpGet.setConfig(getConfig());
            CloseableHttpResponse response = httpclient.execute(httpGet);

            HttpEntity entity = response.getEntity();
            BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), Consts.UTF_8));
            StringBuffer sb = new StringBuffer();
            String content = null;
            while ((content = reader.readLine()) != null) {
                sb.append(content);
            }
            result = sb.toString();

            return new HR(response.getStatusLine().getStatusCode(), result);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (httpclient != null) {
                httpclient.close();
            }
        }
    }

    /**
     * @param url
     * @param data
     * @param header 自定义请求头
     * @return
     * @throws Exception
     * @Description Content-Type:"application/json"类型的get请求
     */
    public static HR toGetJson(String url, Map<String, String> data, Map<String, String> header) throws Exception {
        CloseableHttpClient httpclient = null;
        String result = null;
        try {
            httpclient = HttpClients.custom()
                    .setConnectionManager(createConnectionManager())
                    .build();
            StringBuffer buf = new StringBuffer();
            //拼装请求参数
            if (!CollectionUtils.isEmpty(data)) {
                data.keySet().stream().forEach(key -> {
                    buf.append("&").append(key).append("=").append(data.get(key));
                });
            }
            String params = buf.toString().replaceFirst("&", "");
            url = url.contains("?") ? url + params : url + "?" + params;
            //初始化get请求函数
            HttpGet httpGet = new HttpGet(url);

            //设置请求头
            if (!CollectionUtils.isEmpty(header)) {
                header.keySet().stream().forEach(key -> {
                    httpGet.setHeader(key, header.get(key));
                });
            }
            httpGet.setHeader("Content-Type", "application/json");
            httpGet.setHeader("charset", Consts.UTF_8.toString());
            httpGet.setConfig(getConfig());
            CloseableHttpResponse response = httpclient.execute(httpGet);

            HttpEntity entity = response.getEntity();
            BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), Consts.UTF_8));
            StringBuffer sb = new StringBuffer();
            String content = null;
            while ((content = reader.readLine()) != null) {
                sb.append(content);
            }
            result = sb.toString();

            return new HR(response.getStatusLine().getStatusCode(), result);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (httpclient != null) {
                httpclient.close();
            }
        }
    }


    /**
     * @param url url+参数组合的请求地址
     * @return
     * @throws Exception
     * @Description Content-Type:"application/x-www-form-urlencoded"类型的get请求
     */
    public static HR toGetForm(String url) throws Exception {
        CloseableHttpClient httpclient = null;
        String result = null;
        try {
            httpclient = HttpClients.custom()
                    .setConnectionManager(createConnectionManager())
                    .build();
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("Content-Type", "application/x-www-form-urlencoded");
            httpGet.setHeader("charset", Consts.UTF_8.toString());
            httpGet.setConfig(getConfig());
            CloseableHttpResponse response = httpclient.execute(httpGet);

            HttpEntity entity = response.getEntity();
            BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), Consts.UTF_8));
            StringBuffer sb = new StringBuffer();
            String content = null;
            while ((content = reader.readLine()) != null) {
                sb.append(content);
            }
            result = sb.toString();

            return new HR(response.getStatusLine().getStatusCode(), result);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (httpclient != null) {
                httpclient.close();
            }
        }
    }

    /**
     * @param url
     * @param header
     * @return
     * @throws Exception
     * @Description "Content-Type":"application/x-www-form-urlencoded"类型的get请求
     */
    public static HR toGetFormContainsHeader(String url, Map<String, String> header) throws Exception {
        CloseableHttpClient httpclient = null;
        String result = null;
        try {
            httpclient = HttpClients.custom()
                    .setConnectionManager(createConnectionManager())
                    .build();
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("Content-Type", "application/x-www-form-urlencoded");
            httpGet.setHeader("charset", "utf-8");
            if (!CollectionUtils.isEmpty(header)) {
                header.keySet().forEach(e -> {
                    httpGet.setHeader(e, header.get(e));
                });
            }
            httpGet.setConfig(getConfig());
            CloseableHttpResponse response = httpclient.execute(httpGet);

            HttpEntity entity = response.getEntity();
            BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), Consts.UTF_8));
            StringBuffer sb = new StringBuffer();
            String content = null;
            while ((content = reader.readLine()) != null) {
                sb.append(content);
            }
            result = sb.toString();

            return new HR(response.getStatusLine().getStatusCode(), result);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (httpclient != null) {
                httpclient.close();
            }
        }
    }

    /**
     * @param url
     * @param data
     * @param header 自定义请求头
     * @return
     * @throws Exception
     * @Description Content-Type:"application/json"类型的get请求
     */
    public static HR toGetForm(String url, Map<String, String> data, Map<String, String> header) throws Exception {
        CloseableHttpClient httpclient = null;
        String result = null;
        try {
            httpclient = HttpClients.custom()
                    .setConnectionManager(createConnectionManager())
                    .build();
            StringBuffer buf = new StringBuffer();
            //拼装请求参数
            if (!CollectionUtils.isEmpty(data)) {
                data.keySet().stream().forEach(key -> {
                    buf.append("&").append(key).append("=").append(data.get(key));
                });
            }
            String params = buf.toString().replaceFirst("&", "");
            url = url.contains("?") ? url + params : url + "?" + params;
            //初始化get请求函数
            HttpGet httpGet = new HttpGet(url);

            //设置请求头
            if (!CollectionUtils.isEmpty(header)) {
                header.keySet().stream().forEach(key -> {
                    httpGet.setHeader(key, header.get(key));
                });
            }
            httpGet.setHeader("Content-Type", "application/x-www-form-urlencoded");
            httpGet.setHeader("charset", Consts.UTF_8.toString());
            httpGet.setConfig(getConfig());
            CloseableHttpResponse response = httpclient.execute(httpGet);

            HttpEntity entity = response.getEntity();
            BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), Consts.UTF_8));
            StringBuffer sb = new StringBuffer();
            String content = null;
            while ((content = reader.readLine()) != null) {
                sb.append(content);
            }
            result = sb.toString();

            return new HR(response.getStatusLine().getStatusCode(), result);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (httpclient != null) {
                httpclient.close();
            }
        }
    }

    /**
     * @param url url+参数组合的请求地址
     * @return
     * @throws Exception
     * @Description Content-Type:"application/xml"类型的get请求
     */
    public static HR toGetXml(String url) throws Exception {
        CloseableHttpClient httpclient = null;
        String result = null;
        try {
            httpclient = HttpClients.custom()
                    .setConnectionManager(createConnectionManager())
                    .build();
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("Content-Type", "application/xml");
            httpGet.setHeader("charset", Consts.UTF_8.toString());
            httpGet.setConfig(getConfig());
            CloseableHttpResponse response = httpclient.execute(httpGet);

            HttpEntity entity = response.getEntity();
            BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), Consts.UTF_8));
            StringBuffer sb = new StringBuffer();
            String content = null;
            while ((content = reader.readLine()) != null) {
                sb.append(content);
            }
            result = sb.toString();

            return new HR(response.getStatusLine().getStatusCode(), result);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (httpclient != null) {
                httpclient.close();
            }
        }
    }

    /**
     * @param url
     * @param data
     * @return
     * @throws Exception
     * @Description Content-Type:"application/xml"类型的get请求
     */
    public static HR toGetXml(String url, Map<String, String> data) throws Exception {
        CloseableHttpClient httpclient = null;
        String result = null;
        try {
            httpclient = HttpClients.custom()
                    .setConnectionManager(createConnectionManager())
                    .build();
            StringBuffer buf = new StringBuffer();
            //拼装请求参数
            if (!CollectionUtils.isEmpty(data)) {
                data.keySet().stream().forEach(key -> {
                    buf.append("&").append(key).append("=").append(data.get(key));
                });
                ;
            }
            String params = buf.toString().replaceFirst("&", "");
            url = url.contains("?") ? url + params : url + "?" + params;
            //初始化get请求函数
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("Content-Type", "application/xml");
            httpGet.setHeader("charset", Consts.UTF_8.toString());
            httpGet.setConfig(getConfig());
            CloseableHttpResponse response = httpclient.execute(httpGet);

            HttpEntity entity = response.getEntity();
            BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), Consts.UTF_8));
            StringBuffer sb = new StringBuffer();
            String content = null;
            while ((content = reader.readLine()) != null) {
                sb.append(content);
            }
            result = sb.toString();

            return new HR(response.getStatusLine().getStatusCode(), result);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (httpclient != null) {
                httpclient.close();
            }
        }
    }

    /**
     * @param url
     * @param data
     * @param header 自定义请求头
     * @return
     * @throws Exception
     * @Description Content-Type:"application/xml"类型的get请求
     */
    public static HR toGetXml(String url, Map<String, String> data, Map<String, String> header) throws Exception {
        CloseableHttpClient httpclient = null;
        String result = null;
        try {
            httpclient = HttpClients.custom()
                    .setConnectionManager(createConnectionManager())
                    .build();
            StringBuffer buf = new StringBuffer();
            //拼装请求参数
            if (!CollectionUtils.isEmpty(data)) {
                data.keySet().stream().forEach(key -> {
                    buf.append("&").append(key).append("=").append(data.get(key));
                });
            }
            String params = buf.toString().replaceFirst("&", "");
            url = url.contains("?") ? url + params : url + "?" + params;
            //初始化get请求函数
            HttpGet httpGet = new HttpGet(url);

            //设置请求头
            if (!CollectionUtils.isEmpty(header)) {
                header.keySet().stream().forEach(key -> {
                    httpGet.setHeader(key, header.get(key));
                });
            }
            httpGet.setHeader("Content-Type", "application/xml");
            httpGet.setHeader("charset", Consts.UTF_8.toString());
            httpGet.setConfig(getConfig());
            CloseableHttpResponse response = httpclient.execute(httpGet);

            HttpEntity entity = response.getEntity();
            BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), Consts.UTF_8));
            StringBuffer sb = new StringBuffer();
            String content = null;
            while ((content = reader.readLine()) != null) {
                sb.append(content);
            }
            result = sb.toString();

            return new HR(response.getStatusLine().getStatusCode(), result);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (httpclient != null) {
                httpclient.close();
            }
        }
    }

    /**
     * @param data
     * @param url
     * @return
     * @throws Exception
     * @Description POST请求-数据类型JSON
     */
    public static HR toPostJson(String url, String data) throws Exception {
        CloseableHttpClient httpclient = null;
        String result = null;
        try {
            httpclient = HttpClients.custom()
                    .setConnectionManager(createConnectionManager())
                    .build();
            HttpPost httppost = new HttpPost(url);
            // 设置包体参数。
            StringEntity se = new StringEntity(data);
            se.setContentEncoding("UTF-8");
            se.setContentType("application/json");
            httppost.setEntity(se);
            httppost.setConfig(getConfig());
            CloseableHttpResponse response = httpclient.execute(httppost);

            HttpEntity entity = response.getEntity();
            BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), Consts.UTF_8));
            StringBuffer sb = new StringBuffer();
            String content = null;
            while ((content = reader.readLine()) != null) {
                sb.append(content);
            }
            result = sb.toString();

            return new HR(response.getStatusLine().getStatusCode(), result);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (httpclient != null) {
                httpclient.close();
            }
        }
    }

    /**
     * @param data
     * @param url
     * @return
     * @throws Exception
     * @Description POST请求-数据类型JSON
     */
    public static HR toPostJson(String url, String data, Map<String, String> header) throws Exception {
        CloseableHttpClient httpclient = null;
        String result = null;
        try {
            httpclient = HttpClients.custom()
                    .setConnectionManager(createConnectionManager())
                    .build();
            HttpPost httppost = new HttpPost(url);
            // 设置包体参数。
            StringEntity se = new StringEntity(data);
            se.setContentEncoding("UTF-8");
            se.setContentType("application/json");
            httppost.setEntity(se);
            //设置请求头
            if (!CollectionUtils.isEmpty(header)) {
                header.keySet().stream().forEach(key -> {
                    httppost.setHeader(key, header.get(key));
                });
            }
            httppost.setConfig(getConfig());
            CloseableHttpResponse response = httpclient.execute(httppost);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), Consts.UTF_8));
                StringBuffer sb = new StringBuffer();
                String content = null;
                while ((content = reader.readLine()) != null) {
                    sb.append(content);
                }
                result = sb.toString();
            }
            return new HR(response.getStatusLine().getStatusCode(), result);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (httpclient != null) {
                httpclient.close();
            }
        }
    }

    /**
     * @param url
     * @param data
     * @return
     * @throws Exception
     * @Description Content-Type:"application/json"类型的get请求
     */
    public static HR toPostJson(String url, Map<String, String> data) throws Exception {
        CloseableHttpClient httpclient = null;
        String result = null;
        try {
            httpclient = HttpClients.custom()
                    .setConnectionManager(createConnectionManager())
                    .build();
            HttpPost httppost = new HttpPost(url);
            httppost.setHeader("Content-Type", "application/json");
            httppost.setHeader("charset", Consts.UTF_8.toString());
            StringEntity se = new StringEntity(JSONObject.toJSONString(data), Consts.UTF_8.toString());
            se.setContentType("application/json");
            httppost.setEntity(se);
            httppost.setConfig(getConfig());
            CloseableHttpResponse response = httpclient.execute(httppost);

            HttpEntity entity = response.getEntity();
            BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), Consts.UTF_8));
            StringBuffer sb = new StringBuffer();
            String content = null;
            while ((content = reader.readLine()) != null) {
                sb.append(content);
            }
            result = sb.toString();
            return new HR(response.getStatusLine().getStatusCode(), result);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (httpclient != null) {
                httpclient.close();
            }
        }
    }

    /**
     * @param url
     * @param data
     * @param header 自定义请求头
     * @return
     * @throws Exception
     * @Description Content-Type:"application/json"类型的get请求
     */
    public static HR toPostJson(String url, Map<String, String> data, Map<String, String> header) throws Exception {
        CloseableHttpClient httpclient = null;
        String result = null;
        try {
            httpclient = HttpClients.custom()
                    .setConnectionManager(createConnectionManager())
                    .build();
            HttpPost httppost = new HttpPost(url);
            //设置参数
            List<NameValuePair> nvps = new ArrayList<>();
            if (data != null && !data.isEmpty()) {//设置参数
                data.keySet().stream().forEach(e -> {
                    nvps.add(new BasicNameValuePair(e, data.get(e)));
                });
                httppost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8.toString()));
            }

            //设置请求头
            if (!CollectionUtils.isEmpty(header)) {
                header.keySet().stream().forEach(key -> {
                    httppost.setHeader(key, header.get(key));
                });
            }
            StringEntity se = new StringEntity(JSONObject.toJSONString(data), Consts.UTF_8.toString());
            se.setContentType("application/json");
            httppost.setEntity(se);
            httppost.setConfig(getConfig());

            CloseableHttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), Consts.UTF_8));
            StringBuffer sb = new StringBuffer();
            String content = null;
            while ((content = reader.readLine()) != null) {
                sb.append(content);
            }
            result = sb.toString();

            return new HR(response.getStatusLine().getStatusCode(), result);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (httpclient != null) {
                httpclient.close();
            }
        }
    }

    /**
     * @param data
     * @param url
     * @param url
     * @return
     * @throws Exception
     * @Description POST请求-数据类型form表单
     */
    public static HR toPostForm(String url, String data) throws Exception {
        CloseableHttpClient httpclient = null;
        String result = null;
        try {
            httpclient = HttpClients.custom()
                    .setConnectionManager(createConnectionManager())
                    .build();
            HttpPost httppost = new HttpPost(url);
            if (data != null) {
                StringEntity entity = new StringEntity(data);
                entity.setContentEncoding("UTF-8");
                httppost.setEntity(entity);
                httppost.setHeader("Content-Type", "application/x-www-form-urlencoded");
            }
            httppost.setConfig(getConfig());
            CloseableHttpResponse response = httpclient.execute(httppost);

            HttpEntity entity = response.getEntity();
            BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), Consts.UTF_8));
            StringBuffer sb = new StringBuffer();
            String content = null;
            while ((content = reader.readLine()) != null) {
                sb.append(content);
            }
            result = sb.toString();

            return new HR(response.getStatusLine().getStatusCode(), result);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (httpclient != null) {
                httpclient.close();
            }
        }
    }

    /**
     * @param url
     * @param data
     * @return
     * @throws Exception
     * @Description Content-Type:"application/json"类型的get请求
     */
    public static HR toPostForm(String url, Map<String, String> data) throws Exception {
        CloseableHttpClient httpclient = null;
        String result = null;
        try {
            httpclient = HttpClients.custom()
                    .setConnectionManager(createConnectionManager())
                    .build();
            HttpPost httppost = new HttpPost(url);
            httppost.setHeader("Content-Type", "application/x-www-form-urlencoded");
            httppost.setHeader("charset", Consts.UTF_8.toString());
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            // 设置包体参数。
            if (!CollectionUtils.isEmpty(data)) {
                data.keySet().forEach(e -> {
                    NameValuePair nvp = new BasicNameValuePair(e, data.get(e));
                    nvps.add(nvp);
                });
            }
            StringEntity se = new UrlEncodedFormEntity(nvps, Consts.UTF_8);
            httppost.setEntity(se);
            httppost.setConfig(getConfig());
            CloseableHttpResponse response = httpclient.execute(httppost);

            HttpEntity entity = response.getEntity();
            BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), Consts.UTF_8));
            StringBuffer sb = new StringBuffer();
            String content = null;
            while ((content = reader.readLine()) != null) {
                sb.append(content);
            }
            result = sb.toString();

            return new HR(response.getStatusLine().getStatusCode(), result);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (httpclient != null) {
                httpclient.close();
            }
        }
    }

    /**
     * @param url
     * @param header
     * @return
     * @throws Exception
     * @Description "Content-Type":"application/x-www-form-urlencoded"类型的get请求
     */
    public static HR toPostFormContainsHeader(String url, Map<String, String> header) throws Exception {
        CloseableHttpClient httpclient = null;
        String result = null;
        try {
            httpclient = HttpClients.custom()
                    .setConnectionManager(createConnectionManager())
                    .build();
            HttpPost httppost = new HttpPost(url);
            //设置请求头
            if (!CollectionUtils.isEmpty(header)) {
                header.keySet().stream().forEach(key -> {
                    httppost.setHeader(key, header.get(key));
                });
            }
            httppost.setHeader("Content-Type", "application/x-www-form-urlencoded");
            httppost.setHeader("charset", Consts.UTF_8.toString());
            httppost.setConfig(getConfig());
            CloseableHttpResponse response = httpclient.execute(httppost);

            HttpEntity entity = response.getEntity();
            BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), Consts.UTF_8));
            StringBuffer sb = new StringBuffer();
            String content = null;
            while ((content = reader.readLine()) != null) {
                sb.append(content);
            }
            result = sb.toString();

            return new HR(response.getStatusLine().getStatusCode(), result);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (httpclient != null) {
                httpclient.close();
            }
        }
    }

    /**
     * @param url
     * @param data
     * @param header 自定义请求头
     * @return
     * @throws Exception
     * @Description Content-Type:"application/json"类型的get请求
     */
    public static HR toPostForm(String url, Map<String, String> data, Map<String, String> header) throws Exception {
        CloseableHttpClient httpclient = null;
        String result = null;
        try {
            httpclient = HttpClients.custom()
                    .setConnectionManager(createConnectionManager())
                    .build();
            HttpPost httppost = new HttpPost(url);

            //设置请求头
            if (!CollectionUtils.isEmpty(header)) {
                header.keySet().stream().forEach(key -> {
                    httppost.setHeader(key, header.get(key));
                });
            }
            httppost.setHeader("Content-Type", "application/x-www-form-urlencoded");
            httppost.setHeader("charset", Consts.UTF_8.toString());
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            // 设置包体参数。
            if (!CollectionUtils.isEmpty(data)) {
                data.keySet().forEach(e -> {
                    NameValuePair nvp = new BasicNameValuePair(e, data.get(e));
                    nvps.add(nvp);
                });
            }
            StringEntity se = new UrlEncodedFormEntity(nvps, Consts.UTF_8);
            httppost.setEntity(se);
            httppost.setConfig(getConfig());
            CloseableHttpResponse response = httpclient.execute(httppost);

            HttpEntity entity = response.getEntity();
            BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), Consts.UTF_8));
            StringBuffer sb = new StringBuffer();
            String content = null;
            while ((content = reader.readLine()) != null) {
                sb.append(content);
            }
            result = sb.toString();

            return new HR(response.getStatusLine().getStatusCode(), result);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (httpclient != null) {
                httpclient.close();
            }
        }
    }


    /**
     * @param url
     * @param xmlStr
     * @return
     * @throws Exception
     * @Description Content-Type:"application/xml"类型的get请求
     */
    public static HR toPostXml(String url, String xmlStr) throws Exception {
        CloseableHttpClient httpclient = null;
        String result = null;
        try {
            httpclient = HttpClients.custom()
                    .setConnectionManager(createConnectionManager())
                    .build();
            HttpPost httppost = new HttpPost(url);
            httppost.setHeader("Content-Type", "application/xml");
            StringEntity se = new StringEntity(xmlStr, Consts.UTF_8);
            se.setContentEncoding("UTF-8");
            se.setContentType("application/xml");
            httppost.setEntity(se);
            httppost.setConfig(getConfig());
            CloseableHttpResponse response = httpclient.execute(httppost);

            HttpEntity entity = response.getEntity();
            BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), Consts.UTF_8));
            StringBuffer sb = new StringBuffer();
            String content = null;
            while ((content = reader.readLine()) != null) {
                sb.append(content);
            }
            result = sb.toString();

            return new HR(response.getStatusLine().getStatusCode(), result);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (httpclient != null) {
                httpclient.close();
            }
        }
    }

    /**
     * @param url
     * @param xmlStr
     * @param header 自定义请求头
     * @return
     * @throws Exception
     * @Description Content-Type:"application/json"类型的get请求
     */
    public static HR toPostXml(String url, String xmlStr, Map<String, String> header) throws Exception {
        CloseableHttpClient httpclient = null;
        String result = null;
        try {
            httpclient = HttpClients.custom()
                    .setConnectionManager(createConnectionManager())
                    .build();
            HttpPost httppost = new HttpPost(url);
            //设置请求头
            if (!CollectionUtils.isEmpty(header)) {
                header.keySet().stream().forEach(key -> {
                    httppost.setHeader(key, header.get(key));
                });
            }
            httppost.setHeader("Content-Type", "application/xml");
            StringEntity se = new StringEntity(xmlStr, Consts.UTF_8);
            httppost.setEntity(se);
            httppost.setConfig(getConfig());
            CloseableHttpResponse response = httpclient.execute(httppost);

            HttpEntity entity = response.getEntity();
            BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), Consts.UTF_8));
            StringBuffer sb = new StringBuffer();
            String content = null;
            while ((content = reader.readLine()) != null) {
                sb.append(content);
            }
            result = sb.toString();

            return new HR(response.getStatusLine().getStatusCode(), result);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (httpclient != null) {
                httpclient.close();
            }
        }
    }

    /**
     * @param url,headerMap,paramsStr
     * @param url
     * @return
     * @throws Exception
     * @Description PUT请求-数据类型JSON
     */
    public static HR toPutJson(String url, Map<String, String> headerMap, String paramsStr) throws Exception {
        //创建HTTPCLIENT链接对象
        CloseableHttpClient httpclient = null;
        try {
            httpclient = HttpClients.custom()
                    .setConnectionManager(createConnectionManager())
                    .build();
            HttpPut httpPut = new HttpPut(url);
            // 设置头部参数。
            if (headerMap != null) {
                headerMap.forEach((key, val) -> {
                    httpPut.setHeader(key, val);
                });
            }
            if (paramsStr != null) {
                StringEntity se = new StringEntity(paramsStr);
                se.setContentEncoding("UTF-8");
                se.setContentType("application/json");
                httpPut.setEntity(se);
            }
            CloseableHttpResponse response = null;
            String result = null;
            response = httpclient.execute(httpPut);

            HttpEntity entity = response.getEntity();
            BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), Consts.UTF_8));
            StringBuffer sb = new StringBuffer();
            String content = null;
            while ((content = reader.readLine()) != null) {
                sb.append(content);
            }
            result = sb.toString();

            return new HR(response.getStatusLine().getStatusCode(), result);
        } finally {
            if (httpclient != null) {
                httpclient.close();
            }
        }
    }

    /*************************非公用工具类方法，仅供个别游戏单独使用*****************************/

    /**
     * @param xmlStr
     * @param url
     * @param userAgent
     * @return
     * @throws Exception
     * @Description 匹配AG和AGIN游戏使用
     */
    public static HR toPostXml(String xmlStr, String url, String userAgent) throws Exception {
        CloseableHttpClient httpclient = null;
        try {
            httpclient = HttpClients.custom()
                    .setConnectionManager(createConnectionManager())
                    .build();

            HttpPost httppost = new HttpPost(url);
            httppost.addHeader("User-Agent", userAgent);
            // 设置包体参数。
            StringEntity se = new StringEntity(xmlStr);
            se.setContentEncoding("UTF-8");
            se.setContentType("text/xml");
            httppost.setEntity(se);
            httppost.setConfig(getConfig());
            String result = null;
            CloseableHttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), Consts.UTF_8));
            StringBuffer sb = new StringBuffer();
            String content = null;
            while ((content = reader.readLine()) != null) {
                sb.append(content);
            }
            // return sb.toString();
            result = sb.toString();
            return new HR(response.getStatusLine().getStatusCode(), result);
        } finally {
            if (httpclient != null) {
                httpclient.close();
            }
        }
    }

    /**
     * @param url
     * @param data
     * @param header 自定义请求头
     * @return
     * @throws Exception
     * @Description 匹配SW游戏使用
     */
    public static HR toPostFormResponseCode(String url, Map<String, String> data, Map<String, String> header) throws Exception {
        CloseableHttpClient httpclient = null;
        String result = null;
        try {
            httpclient = HttpClients.custom()
                    .setConnectionManager(createConnectionManager())
                    .build();
            HttpPost httppost = new HttpPost(url);

            //设置请求头
            if (!CollectionUtils.isEmpty(header)) {
                header.keySet().stream().forEach(key -> {
                    httppost.setHeader(key, header.get(key));
                });
            }
            httppost.setHeader("Content-Type", "application/x-www-form-urlencoded");
            httppost.setHeader("charset", Consts.UTF_8.toString());
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            // 设置包体参数。
            if (!CollectionUtils.isEmpty(data)) {
                data.keySet().forEach(e -> {
                    NameValuePair nvp = new BasicNameValuePair(e, data.get(e));
                    nvps.add(nvp);
                });
            }
            StringEntity se = new UrlEncodedFormEntity(nvps, Consts.UTF_8);
            httppost.setEntity(se);
            httppost.setConfig(getConfig());
            CloseableHttpResponse response = httpclient.execute(httppost);
            //sw成功状态返回201
            HttpEntity entity = response.getEntity();
            BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), Consts.UTF_8));
            StringBuffer sb = new StringBuffer();
            String content = null;
            while ((content = reader.readLine()) != null) {
                sb.append(content);
            }
            result = sb.toString();

            return new HR(response.getStatusLine().getStatusCode(), result);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (httpclient != null) {
                httpclient.close();
            }
        }
    }


    /**
     * @param url
     * @param data
     * @return
     * @throws Exception
     * @Description 匹配VR彩票使用
     */
    public static HR toPostJsonObjectMap(String url, Map<String, Object> data) throws Exception {
        CloseableHttpClient httpclient = null;
        String result = null;
        try {
            httpclient = HttpClients.custom()
                    .setConnectionManager(createConnectionManager())
                    .build();
            HttpPost httppost = new HttpPost(url);
            httppost.setHeader("Content-Type", "application/json");
            httppost.setHeader("charset", Consts.UTF_8.toString());
            StringEntity se = new StringEntity(JSONObject.toJSONString(data), Consts.UTF_8.toString());
            se.setContentType("application/json");
            httppost.setEntity(se);
            httppost.setConfig(getConfig());
            CloseableHttpResponse response = httpclient.execute(httppost);

            HttpEntity entity = response.getEntity();
            BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), Consts.UTF_8));
            StringBuffer sb = new StringBuffer();
            String content = null;
            while ((content = reader.readLine()) != null) {
                sb.append(content);
            }
            result = sb.toString();

            return new HR(response.getStatusLine().getStatusCode(), result);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (httpclient != null) {
                httpclient.close();
            }
        }
    }


    private static PoolingHttpClientConnectionManager createConnectionManager() throws Exception {
        TrustManager tm = new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
            }

            @Override
            public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
            }
        };
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, new TrustManager[]{tm}, null);

        SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(context,
                NoopHostnameVerifier.INSTANCE);

        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE).register("https", socketFactory).build();

        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(
                socketFactoryRegistry);
        return connectionManager;
    }

    /**
     * @return
     * @MehtodName getConfig
     * @Description 设置请求响应超时时间
     */
    public static RequestConfig getConfig() {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(120 * 1000)
                .setConnectionRequestTimeout(60000)
                .setSocketTimeout(60000)
                .build();
        return requestConfig;
    }
}
