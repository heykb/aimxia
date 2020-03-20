package com.zhu.rimxia.biz.mapper;

import com.zhu.rimxia.biz.model.domain.Video;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface VideoMapper extends MyBaseMapper<Video> {
    @Override
    int insertList(List<Video> list);
}