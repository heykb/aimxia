package com.zhu.rimxia.biz.util;

import com.zhu.rimxia.biz.mapper.MemberMapper;
import com.zhu.rimxia.biz.model.domain.Member;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.validation.constraints.Max;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisUtilTest {

    @Resource
    private MemberMapper memberMapper;
    @Resource
    private  RedisUtil redisUtil;

    @Resource
    private EmailUtil emailUtil;
    @Test
    public void contextLoads() {
        Member member = new Member();
        member.setUserName("fsdfds");
        member.setUserId(1354345L);
        member.setGender(0);
        if(redisUtil == null) System.out.println("1354354");
       redisUtil.set("1354846842",member);

    }
    @Test
    public void get() {

        List<Member> members = memberMapper.selectAll();
        if(members==null){
            System.out.println("null");
        }else if(members.isEmpty()){
            System.out.println("empty");
        }
//        Member member1 = (Member) redisUtil.get("1354846842");
//        //String s = (String)redisUtil.get("13548468421");
//        Long lo = redisUtil.getExpire("1354846842");
//        System.out.println(member1);
//        System.out.println(lo);

    }
    @Test
    public void sendEmail(){
       // emailUtil.sendvalicodeEmail("1259609102@qq.com","1234578");
    }
}