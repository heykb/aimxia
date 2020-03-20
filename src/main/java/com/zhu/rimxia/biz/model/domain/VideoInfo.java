package com.zhu.rimxia.biz.model.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;
import javax.persistence.*;

@Table(name = "video_info")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VideoInfo {
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
    @JsonFormat(pattern="yyyy-MM-dd")
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd")
    @Column(name = "create_time")
    private Date createTime;
}