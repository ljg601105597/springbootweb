package com.ljg.common;

import com.ljg.exception.TokenException;
import io.jsonwebtoken.*;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

/**
 * TOKEN 管理.
 *
 * @author
 * @create 2017-05-18 10:53
 **/
public class TokenManager {
    /**
     * 创建 jwt
     * @param id
     * @param subject
     * @param ttlSeconds
     * @return
     * @throws Exception
     */
    public static String createJWT(String id, String subject,String saltKey, int ttlSeconds) throws TokenException {
        try {
            SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256 ;
            long nowMillis = System. currentTimeMillis();
            Date now = new Date( nowMillis);
            SecretKey key = generalKey(saltKey);
            JwtBuilder builder = Jwts. builder()
                    .setId(id)
                    .setIssuedAt(now)
                    .setSubject(subject)
                    .signWith(signatureAlgorithm, key);
            if (ttlSeconds >= 0){
                long expMillis = nowMillis + ttlSeconds*1000;
                Date exp = new Date( expMillis);
                builder.setExpiration( exp);
            }
            return builder.compact();
        }catch (Exception e){
            throw new TokenException("创建Token失败");
        }

    }

//    public static void main(String[] args) {
//        try {
//            TokenManager tokenManager=new TokenManager();
//            String str=tokenManager.createJWT("999","李金国",100000L);
//            System.out.println(str);
//            Claims claims=tokenManager.parseJWT(str);
//            System.out.println(claims);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }

    /**
     * 解密 jwt
     * @param jwt
     * @return
     * @throws Exception
     */
    public static Claims parseJWT(String jwt,String saltKey) throws TokenException {
        try {
            SecretKey key = generalKey(saltKey);
            Claims claims = Jwts. parser()
                    .setSigningKey( key)
                    .parseClaimsJws( jwt).getBody();
            return claims;
        }catch (Exception e){
            if(e instanceof ExpiredJwtException){
                throw new TokenException("token过期");
            }
            throw new TokenException("解析Token失败");
        }
    }

    /**
     * 由用户saltKey盐值生成加密key
     * @return
     */
    private static SecretKey generalKey(String saltKey){
        byte[] encodedKey = Base64.decodeBase64(saltKey);
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        return key;
    }

}
