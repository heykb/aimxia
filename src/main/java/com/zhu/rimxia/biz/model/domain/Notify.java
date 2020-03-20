package com.zhu.rimxia.biz.model.domain;

import java.util.Date;
import javax.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Notify {
    /**
     * 消息id
     */
    @Id
    @Column(name = "notify_id")
    private Long notifyId;

    /**
     * 内容(公告、私信、提醒伴生实体如评论内容和更新内容)
     */
    private String content;

    /**
     * 类型【1,2,3】公告、提醒、信息
     */
    private Integer type;

    /**
     * 目标id
     */
    @Column(name = "target_id")
    private Long targetId;

    /**
     * 目标类型
     */
    @Column(name = "target_type")
    private String targetType;

    /**
     * 提醒类型<=19被发布订阅||>=19<=39被关注订阅
     */
    @Column(name = "remind_type")
    private Integer remindType;

    /**
     * 发送者id
     */
    @Column(name = "sender_id")
    private Long senderId;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;
}