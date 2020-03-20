package com.zhu.rimxia.biz.model.domain;

import javax.persistence.*;
import lombok.*;

@Table(name = "user_notify")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserNotify {
    /**
     * 用户信息id
     */
    @Id
    @Column(name = "user_notify_id")
    private Long userNotifyId;

    /**
     * 是否已读
     */
    @Column(name = "is_read")
    private Boolean isRead;

    /**
     * 所属用户
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 消息实体
     */
    @Column(name = "notify_id")
    private Long notifyId;
}