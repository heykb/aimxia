package com.zhu.rimxia;

import com.zhu.rimxia.biz.mapper.VideoInfoMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RimxiaApplicationTests {


    @Resource
    private VideoInfoMapper videoInfoMapper;
    @Test
    public void contextLoads() {
        System.out.println(videoInfoMapper.getDetailById(6260262028870656L));;
    }

}
