package com.zhu.rimxia.biz.model.modelVo;

import com.zhu.rimxia.biz.validGroup.AddGroup;
import lombok.*;
import org.hibernate.validator.constraints.Range;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CommentVo {
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
    @NotNull(message = "评论目标不能为空",groups = AddGroup.class)
    private Long targetId;

    /**
     * 目标类型
     */
    @Column(name = "target_type")
    @NotBlank(message = "目标类型不能为空",groups = AddGroup.class)
    private String targetType;
    /**
     * 评论内容
     */
    @NotBlank(message="评论内容不能为空",groups = AddGroup.class)
    private String content;

    /**
     * 用户id
     */
    @Column(name = "createrId")
    @NotNull(message="评论用户不能为空",groups = AddGroup.class)
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
}
