package com.zhu.rimxia.biz.model.domain;

import javax.persistence.*;
import lombok.*;

@Table(name = "subscription_config")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SubscriptionConfig {
    /**
     * 用户id
     */
    @Id
    @Column(name = "user_id")
    private Long userId;

    /**
     * 订阅动作：评论
     */
    private Boolean comment;

    /**
     * 订阅动作：点赞
     */
    private Boolean like;

    /**
     * 订阅动作：创建
     */
    private Boolean create;

    /**
     * 【评论0/1+点赞0/1+更新0/1+被更新0+被回复0/1】
     */
    @Column(name = "comment_detail")
    private String commentDetail;

    /**
     * 【评论0/1+点赞0/1+更新0/1】
     */
    @Column(name = "like_detail")
    private String likeDetail;

    /**
     * 【评论0/1+点赞0/1+更新0/1+被更新0/1+被回复0/1】
     */
    @Column(name = "create_detail")
    private String createDetail;

    /**
     * 订阅目标类型【动漫、评论】
     */
    @Column(name = "target_type")
    private String targetType;
}