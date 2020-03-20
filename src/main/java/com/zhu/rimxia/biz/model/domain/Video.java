package com.zhu.rimxia.biz.model.domain;

import java.util.Date;
import javax.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Video {
    /**
     * 视频id
     */
    @Id
    @Column(name = "video_id")
    private Long videoId;

    /**
     * 视频地址
     */
    private String url;

    /**
     * 正片为0预告片为1，其他为2
     */
    private Integer type;

    /**
     * 第几集
     */
    private Integer sort;

    /**
     * 视频信息id
     */
    @Column(name = "video_info_id")
    private Long videoInfoId;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 集名
     */
    @Column(name = "video_name")
    private String videoName;
}