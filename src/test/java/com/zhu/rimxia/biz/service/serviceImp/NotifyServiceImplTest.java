package com.zhu.rimxia.biz.service.serviceImp;

import com.zhu.rimxia.biz.model.domain.Notify;
import com.zhu.rimxia.biz.service.NotifyService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class NotifyServiceImplTest {

    @Resource
    private NotifyService notifyService;
    @Test
    public void pullNewRemind() {
         //notifyService.createSubscription(59471142195200L,123456L,"comment","sub");
        notifyService.createSubscription(594711421952L,123456L,"comment","sub");
        notifyService.createSubscription(59471142195200L,1234561235L,"comment","sub");
         notifyService.createRemind(123456L,"comment",2,59471142195200L,"你好");
//        notifyService.createRemind(12345L,"comment",2,59471142195200L,"1324864145");
       // notifyService.pullNewRemind(59471142195200L);



    }
    @Test
    public void fins() {
       List<Notify> notifyList = notifyService.find(59471142195200L,123456L,NotifyService.REMIND_TYPE_COMMENT);
        System.out.println(notifyList.isEmpty());

    }
}