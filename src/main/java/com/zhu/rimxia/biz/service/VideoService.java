package com.zhu.rimxia.biz.service;


import com.zhu.rimxia.biz.model.domain.Video;
import com.zhu.rimxia.biz.model.modelVo.VideoVo;

import java.util.List;

public interface VideoService {
    List<Video> addList(List<VideoVo> videoVos);

    Video getById(Long videoId);

    Video addNewEpisode(VideoVo videoVo);
}
