package com.zhu.rimxia.biz.mapper;

import com.zhu.rimxia.biz.model.domain.VideoInfo;
import com.zhu.rimxia.biz.model.modelDo.VideoInfoDo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface VideoInfoMapper extends MyBaseMapper<VideoInfo> {
    VideoInfoDo getDetailById(Long videoInfoId);
}