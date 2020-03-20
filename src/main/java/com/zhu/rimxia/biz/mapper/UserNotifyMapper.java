package com.zhu.rimxia.biz.mapper;

import com.zhu.rimxia.biz.model.domain.UserNotify;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserNotifyMapper extends MyBaseMapper<UserNotify> {
    @Override
    int insertList(List<UserNotify> list);
}