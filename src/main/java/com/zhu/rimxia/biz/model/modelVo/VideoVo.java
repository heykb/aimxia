package com.zhu.rimxia.biz.model.modelVo;

import com.zhu.rimxia.biz.validGroup.AddGroup;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VideoVo {
    /**
     * 视频id
     */

    private Long videoId;

    /**
     * 视频信息id
     */
    @NotNull(groups = AddGroup.class)
    private Long videoInfoId;

    /**
     * 视频
     */
    @NotNull(groups = AddGroup.class)
    private MultipartFile videoFile;

    /**
     * 正片为0预告片为1，其他为2
     */
    @NotNull(groups = AddGroup.class)
    private Integer type;

    /**
     * 第几集
     */
    @NotNull(groups = AddGroup.class)
    private Integer sort;

    private Date createTime;

    private String videoName;

}
