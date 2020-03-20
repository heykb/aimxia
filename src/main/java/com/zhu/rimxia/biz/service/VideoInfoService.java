package com.zhu.rimxia.biz.service;

import com.zhu.rimxia.biz.model.domain.VideoInfo;
import com.zhu.rimxia.biz.model.modelDo.VideoInfoDo;
import com.zhu.rimxia.biz.model.modelVo.VideoInfoVo;

import java.util.List;

public interface VideoInfoService {

    VideoInfo add(VideoInfoVo videoInfoVo);

    VideoInfo getById(Long videoInfoId);

    VideoInfoDo getDetailById(Long videoInfoId);

    List<VideoInfo> selectList(Integer pageNum, Integer pageSize);
}
