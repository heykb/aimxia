package com.zhu.rimxia.biz.service;

import com.zhu.rimxia.biz.model.domain.Notify;
import com.zhu.rimxia.biz.model.domain.SubscriptionConfig;
import com.zhu.rimxia.biz.model.domain.UserNotify;

import java.util.List;

public interface NotifyService {

    public final int REMIND_TYPE_LIKE=1;
    public final int REMIND_TYPE_COMMENT=2;
    public final int REMIND_TYPE_FOLLOW=3;
    public final int REMIND_TYPE_HATE=4;
    public final int REMIND_TYPE_FORWORD=5;
    public final int REMIND_TYPE_ANSWER=6;
    public final int REMIND_TYPE_REPORT=7;
    public final int REMIND_TYPE_DELETE1=19;
    public final int REMIND_TYPE_UPDATE=20;
    public final int REMIND_TYPE_HOT_ANSWER=21;
    public final int REMIND_TYPE_DELETE2= 22;

    void createSubscription(Long userId, Long targetId, String targetType, String subWay);

    Notify createRemind(Long targetId, String targetType, int remindType, Long senderId, String content);

    List<UserNotify> pushNewRemindToUser(Notify notify);

    void pullNewRemind(Long userId);

    Notify createAnnounce(String content);

    List<Notify> find(Long sender,Long targetId,int remindType);
}
