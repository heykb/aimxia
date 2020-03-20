package com.zhu.rimxia.biz.model.domain;

import java.util.Date;
import javax.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Comment {
    /**
     * 评论id
     */
    @Id
    @Column(name = "comment_id")
    private Long commentId;

    /**
     * 目标id
     */
    @Column(name = "target_id")
    private Long targetId;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 创建者
     */
    @Column(name = "creater_id")
    private Long createrId;

    /**
     * 点赞数
     */
    @Column(name = "like_num")
    private Integer likeNum;

    /**
     * 用户头像
     */
    @Column(name = "user_head_img")
    private String userHeadImg;

    /**
     * 评论时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 是否作者
     */
    @Column(name = "is_auth")
    private Boolean isAuth;

    /**
     * 目标类型
     */
    @Column(name = "target_type")
    private String targetType;
}