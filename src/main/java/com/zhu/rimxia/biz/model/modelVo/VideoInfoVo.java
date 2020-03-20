package com.zhu.rimxia.biz.model.modelVo;

import com.zhu.rimxia.biz.model.domain.Video;
import com.zhu.rimxia.biz.validGroup.AddGroup;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.xml.ws.soap.AddressingFeature;
import java.util.Date;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VideoInfoVo {

    /**
     * 视频id
     */
    @Id
    @Column(name = "video_info_id")
    private Long videoInfoId;

    /**
     * 视频名
     */
    @Column(name = "video_name")
    @NotNull(message = "视频名不能为空",groups = AddGroup.class)
    private String videoName;

    /**
     * 类型
     */
    @NotNull(message = "视频类型不能为空",groups = AddGroup.class)
    private String type;

    /**
     * 地区
     */

    private String addr;

    /**
     * 集数
     */
    private Integer episodes;

    /**
     * 简介
     */
    @NotNull(message = "视频简介不能为空",groups = AddGroup.class)
    private String summary;

    /**
     * 更新到
     */
    @Column(name = "new_episode")
    private Integer newEpisode;

    /**
     * 视频封面图片url
     */
    @NotBlank(message = "视频封面图片不能为空",groups = AddGroup.class)
    private String cover;

    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

}
