package com.zhu.rimxia.biz.controller;

import com.zhu.rimxia.biz.common.JsonData;
import com.zhu.rimxia.biz.common.ValidList;
import com.zhu.rimxia.biz.model.domain.Video;
import com.zhu.rimxia.biz.model.modelVo.VideoVo;
import com.zhu.rimxia.biz.service.NotifyService;
import com.zhu.rimxia.biz.service.VideoService;
import com.zhu.rimxia.biz.validGroup.AddGroup;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Api(description = "某动漫视频相关")
@RequestMapping("/video")
@RestController
public class VideoController {

    @Resource
    private VideoService videoService;

    @Resource
    private NotifyService notifyService;
    @ApiOperation("添加最新集视频列表")
    @PostMapping("/addList")
    public JsonData<List<Video>> addList(@RequestBody @Validated({AddGroup.class}) ValidList<VideoVo> videoVos){
        List<Video> videos = videoService.addList(videoVos);
        if(!videos.isEmpty()){
            notifyService.createRemind(videos.get(0).getVideoInfoId(),"anime",NotifyService.REMIND_TYPE_UPDATE,null,null);
        }
        return JsonData.ok();
    }



}
