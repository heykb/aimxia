package com.zhu.rimxia.biz.service.serviceImp;

import com.zhu.rimxia.biz.exception.BusinessException;
import com.zhu.rimxia.biz.exception.CommonErrorCode;
import com.zhu.rimxia.biz.mapper.NotifyMapper;
import com.zhu.rimxia.biz.mapper.SubscriptionMapper;
import com.zhu.rimxia.biz.mapper.UserNotifyMapper;
import com.zhu.rimxia.biz.model.domain.Notify;
import com.zhu.rimxia.biz.model.domain.Subscription;
import com.zhu.rimxia.biz.model.domain.UserNotify;
import com.zhu.rimxia.biz.service.MemberService;
import com.zhu.rimxia.biz.service.NotifyService;
import com.zhu.rimxia.biz.util.id.IdUtil;
import org.aspectj.weaver.ast.Not;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

@Service
public class NotifyServiceImpl  implements NotifyService {

    @Resource
    private MemberService memberService;

    @Resource
    private SubscriptionMapper subscriptionMapper;

    @Resource
    private NotifyMapper notifyMapper;

    @Resource
    private UserNotifyMapper userNotifyMapper;

    /**
     * 创建公告
     * @param content
     * @return
     */
    @Override
    public Notify createAnnounce(String content) {

        return null;
    }



    /**
     * 创建订阅
     * @param userId
     * @param targetId
     * @param targetType
     * @param subWay 发布、关注
     */
    @Override
    public void createSubscription(Long userId, Long targetId, String targetType, String subWay) {
        memberService.getById(userId);
        Date now = new Date();
        Subscription subscription = Subscription.builder()
                .targetId(targetId)
                .targetType(targetType)
                .subWay(subWay)
                .userId(userId)
                .createTime(now)
                .lastPullTime(now)
                .build();
        subscriptionMapper.insert(subscription);
    }

    /**
     * 创建提醒
     * @param targetId
     * @param targetType
     * @param remindType
     * @param senderId
     */
    @Override
    public Notify createRemind(Long targetId, String targetType, int remindType, Long senderId, String content) {
        if(senderId!=null){
            memberService.getById(senderId);
        }
        Notify notify = Notify.builder()
                .notifyId(IdUtil.generateId())
                .type(2)
                .targetType(targetType)
                .targetId(targetId)
                .remindType(remindType)
                .content(content)
                .createTime(new Date())
                .senderId(senderId)
                .build();
        notifyMapper.insert(notify);
        return notify;
    }



    //推送新消息给订阅的用户
    @Override
    public List<UserNotify> pushNewRemindToUser(Notify notify){

        if(notify == null){
            return new ArrayList<>();
        }
        int remindType = notify.getRemindType();
        Long senderId = notify.getSenderId();
        Example example = new Example(Subscription.class);
        Example.Criteria criteria = example.createCriteria();
        if(remindType<15){
            criteria.andEqualTo("targetId",notify.getTargetId());
            criteria.andEqualTo("subWay","sub");
        }else if(remindType<20){
            criteria.andEqualTo("targetId",notify.getTargetId());
            criteria.andEqualTo("subWay","sub");
            Example.Criteria criteria2 = example.createCriteria();
            criteria2.andEqualTo("targetId",notify.getTargetId());
            criteria2.andEqualTo("subWay","follow");
            example.or(criteria2);
        }else if(remindType < 35){
            criteria.andEqualTo("targetId",notify.getTargetId());
            criteria.andEqualTo("subWay","follow");
        }

        List<Subscription> subscriptions = subscriptionMapper.selectByExample(example);;
        List<UserNotify> userNotifies = new ArrayList<>();
        for (Subscription subscription:subscriptions) {
            if (filer(remindType, senderId, subscription)) {
                Long userId = subscription.getUserId();
                UserNotify userNotify = UserNotify.builder()
                        .userId(userId)
                        .isRead(false)
                        .userNotifyId(IdUtil.generateId())
                        .notifyId(notify.getNotifyId())
                        .build();
                userNotifies.add(userNotify);
            }
        }
        if(!userNotifies.isEmpty()){
            userNotifyMapper.insertList(userNotifies);
        }
        return userNotifies;
    }

    //过滤
    private boolean filer(int remindType,Long senderId, Subscription subscription){
        boolean re = true;
        Long userId = subscription.getUserId();
        //读取配置文件
        String filename = userId+".properties";
        File configFile = new File("D:\\FTP\\userRemindConfig\\"+filename);
        if(!configFile.exists()){
            configFile = new File("D:\\FTP\\userRemindConfig\\defaultUserRemindConfig.properties");
        }
        Properties pro = new Properties();
        try( BufferedReader bufferedReader = new BufferedReader(
                new FileReader(configFile))){

            pro.load(bufferedReader);
            String key = subscription.getSubWay()+"."+subscription.getTargetType()+"."+remindType;
            String conf = pro.getProperty(key);
            System.out.println(key+":::"+conf);
          /*  if(conf==null){
                pro.setProperty(key,"1");
                conf="1";
            }*/
            if(conf.contains("0")){
                re =  false;
            }else if(conf.contains("2") && senderId!=null){
                //不是我关注的人
                if(true){
                    re = false;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return re;
    }



    /**
     * 拉取最新的提醒到用户队列
     * @param userId
     */
    @Override
    public void pullNewRemind(Long userId) {

        Example example = new Example(Subscription.class);
        example.createCriteria().andEqualTo("userId",userId);
        Date now = new Date();
        //获取订阅记录
        List<Subscription> subscriptions = subscriptionMapper.selectByExample(example);
        if(!subscriptions.isEmpty()){
            //读取配置文件
            String filename = userId+".properties";
            File configFile = new File("D:\\FTP\\userRemindConfig\\"+filename);
            if(!configFile.exists()){
                configFile = new File("D:\\FTP\\userRemindConfig\\defaultUserRemindConfig.properties");
            }
            Properties pro = new Properties();
            try( BufferedReader bufferedReader = new BufferedReader(
                    new FileReader(configFile))) {
                pro.load(bufferedReader);

                //根据订阅记录拉去提醒并过滤
                List<UserNotify> userNotifies = new ArrayList<>();
                for (Subscription subscription:subscriptions){
                    //目标id
                    Long targetId = subscription.getTargetId();
                    //订阅方式
                    String subWay = subscription.getSubWay();
                    Example example1 = new Example(Notify.class);
                    Example.Criteria criteria = example1.createCriteria();
                    criteria.andEqualTo("targetId",targetId)
                            .andGreaterThan("createTime",subscription.getLastPullTime());
                    if("sub".equals(subWay)){
                        criteria.andLessThanOrEqualTo("remindType",19);
                    }else if("follow".equals(subWay)){
                        criteria.andGreaterThanOrEqualTo("remindType",19);
                    }
                    //查询该条订阅记录关联的所有提醒
                    List<Notify> notifies = notifyMapper.selectByExample(example1);
                    //过滤提醒返回用户消息列表
                    userNotifies.addAll(remindFilter(notifies,userId,subWay,pro));
                }
                //更新订阅记录的拉取时间
                Example example2 = new Example(Subscription.class);
                example2.createCriteria().andEqualTo("userId",userId)
                        .andLessThan("lastPullTime",now);
                subscriptionMapper.updateByExampleSelective(Subscription.builder().lastPullTime(now).build(),example2);
                //插入用户队列
                if(!userNotifies.isEmpty()){
                    userNotifyMapper.insertList(userNotifies);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 根据用户配置文件过滤提醒
     * @param sources
     * @param userId
     * @return
     */
    private List<UserNotify> remindFilter(List<Notify> sources, Long userId, String subWay,Properties pro){
        List<UserNotify> userNotifies = new ArrayList<>();
        for(Notify notify: sources){
            String key = subWay+"."+notify.getTargetType()+"."+notify.getRemindType();
            String conf = pro.getProperty(key);
            System.out.println(key+":::"+conf);
          /*  if(conf==null){
                pro.setProperty(key,"1");
                conf="1";
            }*/
            if(conf.contains("0")){
                System.out.println("过滤记录："+notify.getNotifyId());
                continue;
            }else if(conf.contains("2") && notify.getSenderId()!=null){
                //不是我关注的人
                if(true){
                    System.out.println("过滤记录："+notify.getNotifyId());
                    continue;
                }
            }
            UserNotify userNotify = UserNotify.builder()
                    .userId(userId)
                    .isRead(false)
                    .userNotifyId(IdUtil.generateId())
                    .notifyId(notify.getNotifyId())
                    .build();
            userNotifies.add(userNotify);

        }
        return userNotifies;
    }


    @Override
    public List<Notify> find(Long sender, Long targetId, int remindType) {
        Example example = new Example(Notify.class);
        example.createCriteria().andEqualTo("senderId",sender)
                .andEqualTo("remindType",remindType)
                .andEqualTo("targetId",targetId);
        return  notifyMapper.selectByExample(example);

    }
}
