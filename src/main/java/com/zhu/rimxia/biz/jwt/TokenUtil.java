package com.zhu.rimxia.biz.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

public class TokenUtil {

    //默认过期时间，单位秒
    public static final long DEFAULT_TOKEN_EXPIRE = 60*60*24L;



    public static String buildToken(String subject, Key key, long expire){
        Date expireDate = new Date(System.currentTimeMillis()+1000*DEFAULT_TOKEN_EXPIRE);
        String accessToken = Jwts.builder()
                .setSubject(subject)
                //.setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(key)
                .compact();
        return accessToken;
    }

    //解析accessToken返回subject
    public static String parseToken(String accessToken, String tokenHexKey){
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(parseHexKey(tokenHexKey)).parseClaimsJws(accessToken);
        return claimsJws.getBody().getSubject();
    }



    //将秘钥解析成token需要的秘钥类型
    public static Key parseHexKey(String tokeHexKey) {
        if (tokeHexKey == null || tokeHexKey.trim().isEmpty()) {
            return null;
        }
        SecretKey key = Keys.hmacShaKeyFor(Hex.decode(tokeHexKey));
        return key;
    }


    private static Key getSecretKey(){
        return Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    /**
     * 生成SignatureAlgorithm.HS256算法加密密钥，并进行16进制编码返回字符串密钥
     * @return
     */
    public static  String getHexKey(){
        return Hex.encode(getSecretKey().getEncoded());
    }


}
