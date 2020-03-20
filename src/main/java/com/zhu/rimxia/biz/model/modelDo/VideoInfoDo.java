package com.zhu.rimxia.biz.model.modelDo;

import com.zhu.rimxia.biz.model.domain.Video;
import lombok.*;
import org.apache.catalina.LifecycleState;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VideoInfoDo {
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
    private String videoName;

    /**
     * 类型
     */
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
    private String summary;

    /**
     * 更新到
     */
    @Column(name = "new_episode")
    private Integer newEpisode;

    /**
     * 视频封面图片url
     */
    private String cover;

    /**
     * 最近修改
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    private List<Video> videos;
}
