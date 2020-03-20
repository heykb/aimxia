package com.zhu.rimxia.biz.jwt;

import com.zhu.rimxia.biz.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class RedisTokenStore {
    //jwt加密秘钥
    public static final String TOKEY_KEY = "oauth_token_key";
    //token的redis key前缀
    public static final String KEY_PRE_TOKEN = "oauth_token:";

    @Resource
    private RedisUtil redisUtil;


    public   String getTokeyKey() {
        String hexKey = (String)redisUtil.get(TOKEY_KEY);
        if(hexKey == null){
            hexKey = TokenUtil.getHexKey();
            redisUtil.set(TOKEY_KEY,hexKey);
        }
        return hexKey;
    }

    /**
     * 向redis注册新token
     * @param userId
     * @param permissions
     * @param roles
     * @return
     */
    public Token registerToken(String userId, String[] permissions, String[] roles){
        String accessToken = TokenUtil.buildToken(
                userId,
                TokenUtil.parseHexKey(getTokeyKey()),
                TokenUtil.DEFAULT_TOKEN_EXPIRE);
        Token newToken = Token.builder()
                .memberId(userId)
                .accessToken(accessToken)
                .permissions(permissions)
                .roles(roles)
                .build();
        System.out.println("11111111111111111111"+KEY_PRE_TOKEN+userId);
        redisUtil.set(KEY_PRE_TOKEN+userId,newToken,TokenUtil.DEFAULT_TOKEN_EXPIRE);
        return newToken;
    }

    public Token findToken(String userId,String accessToken){
        Token token = (Token)redisUtil.get(KEY_PRE_TOKEN+userId);
        if(token!=null && token.getAccessToken().equals(accessToken)){
               return token;
        }
        return null;
    }


}
