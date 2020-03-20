package com.zhu.rimxia.biz.jwt;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTokenStoreTest {

    @Resource
    private RedisTokenStore redisTokenStore;

    @Test
    public void registerToken() {
        Token token = redisTokenStore.registerToken("123456",null,null);
        System.out.println(token.getAccessToken());;
       Token token1 = redisTokenStore.findToken("123456",token.getAccessToken());
        System.out.println(token1.getMemberId());

    }

    @Test
    public void findToken() {

    }
}