package com.zhu.rimxia.biz.model.domain;

import java.util.Date;
import javax.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Subscription {
    /**
     * 目标id
     */
    @Id
    @Column(name = "target_id")
    private Long targetId;

    /**
     * 订阅方式
     */
    @Id
    @Column(name = "sub_way")
    private String subWay;

    /**
     * 用户id
     */
    @Id
    @Column(name = "user_id")
    private Long userId;

    /**
     * 目标类型
     */
    @Column(name = "target_type")
    private String targetType;

    /**
     * 订阅时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 上一次推送到消息队列的时间
     */
    @Column(name = "last_pull_time")
    private Date lastPullTime;
}