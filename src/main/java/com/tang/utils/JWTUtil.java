package com.tang.utils;

import com.tang.base.consts.CacheKeyConsts;
import com.tang.base.consts.CacheTimeConsts;
import io.jsonwebtoken.*;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SignatureException;
import java.util.Base64;
import java.util.Date;

/**
 * 
 * @ClassName JWTUtil
 * @Description jwt签名工具类
 * @author 芙蓉王
 * @Date Dec 28, 2019 12:12:59 PM
 * @version 1.0.0
 */
public class JWTUtil {
	
	private static final String JWT_SECERT = "?.tang8208208820:)jt:(";
	
	 /**
     * 签发JWT
     * @param id
     * @param subject 可以是JSON数据 尽可能少
     * @return  String
     *
     */
    public static String createJWT(String id,String subject) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        SecretKey secretKey = generalKey();
        JwtBuilder builder = Jwts.builder()
                .setId(id)
                .setSubject(subject)   // 主题
                .setIssuer(CacheKeyConsts.JWT_ISSUER)     // 签发者
                .setIssuedAt(now)      // 签发时间
                .signWith(signatureAlgorithm, secretKey); // 签名算法以及密匙
        long expMillis = nowMillis + CacheTimeConsts.JWT_EXPIRED_TIME;
        Date expDate = new Date(expMillis);
        builder.setExpiration(expDate); // 过期时间
        return builder.compact();
    }
    
    
    /**
     * 验证JWT,并获取用户登录账号名
     * @param jwtStr
     * @return
     */
    public static Claims validateJWT(String jwtStr) throws Exception{
        try {
        	Claims claims = parseJWT(jwtStr);
            return claims;
        } catch (ExpiredJwtException e) {
            throw new JwtException("令牌过期");
        } catch (SignatureException e) {
            throw new SignatureException("令牌验证失败");
        } catch (Exception e) {
        	throw new Exception("令牌验证失败");
        }
    }
    
    
    public static SecretKey generalKey() {
        byte[] encodedKey = Base64.getMimeDecoder().decode(JWT_SECERT);
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        return key;
    }
    
    /**
     * 
     * 解析JWT字符串
     * @param jwt
     * @return
     * @throws Exception
     */
    public static Claims parseJWT(String jwt) throws Exception {
        SecretKey secretKey = generalKey();
        return Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(jwt)
            .getBody();
    }
    
    public static void main(String[] args) throws Exception {
		String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIzN2M4MGRkZi03NDM3LTQ4NmQtOTkzNi0xOTJhM2ZjNzQ0N2IiLCJzdWIiOiJ7XCJ1aWRcIjpcIjg0MzE4NzY4MlwiLFwiY2FnZW50XCI6XCJ5aGhcIixcInVzZXJuYW1lXCI6XCJDMzNBNDk4OUEzMzM3Qjg4XCJ9IiwiaXNzIjoiYXV0aG9yaXphdGlvbiIsImlhdCI6MTU3NzUyMjA0OSwiZXhwIjoxNTc3NTI5MjQ5fQ.Roiyq_xGZHNKC4Kuta8cG1nGSzFHYuThlHPBgz4Pxik";
		Claims claims = validateJWT(token);
		String str = "eyjhbgcioijiuzi1nij9.eyjqdgkioiizn2m4mgrkzi03ndm3ltq4nmqtotkzni0xotjhm2zjnzq0n2iilcjzdwiioij7xcj1awrcijpcijg0mze4nzy4mlwilfwiy2fnzw50xci6xcj5aghciixcinvzzxjuyw1lxci6xcjdmznbndk4ouezmzm3qjg4xcj9iiwiaxnzijoiyxv0ag9yaxphdglvbiisimlhdci6mtu3nzuymja0oswizxhwijoxntc3nti5mjq5fq.roiyq_xgzhnkc4kuta8cg1ngszfhyuthlhpbgz4pxik";
		System.err.println(token.equals(str));
		System.err.println(claims.toString());
    }
    
}
