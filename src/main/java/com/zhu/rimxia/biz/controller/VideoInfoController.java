package com.zhu.rimxia.biz.controller;

import com.zhu.rimxia.biz.common.JsonData;
import com.zhu.rimxia.biz.model.domain.Video;
import com.zhu.rimxia.biz.model.domain.VideoInfo;
import com.zhu.rimxia.biz.model.modelDo.VideoInfoDo;
import com.zhu.rimxia.biz.model.modelVo.VideoInfoVo;
import com.zhu.rimxia.biz.model.modelVo.VideoVo;
import com.zhu.rimxia.biz.service.NotifyService;
import com.zhu.rimxia.biz.service.VideoInfoService;
import com.zhu.rimxia.biz.service.VideoService;
import com.zhu.rimxia.biz.validGroup.AddGroup;
import io.swagger.annotations.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Api(description = "动漫介绍")
@RequestMapping("/VideoInfo")
@RestController
public class VideoInfoController {


    @Resource
    private VideoInfoService videoInfoService;
    @Resource
    private NotifyService notifyService;

    @Resource
    private VideoService videoService;

    /**
     * 新增动漫
     * @param videoInfoVo
     * @return
     */
    @ApiOperation("添加动漫")
    @PostMapping("/add")
    public JsonData<VideoInfo> add(@RequestBody @Validated(AddGroup.class) VideoInfoVo videoInfoVo){
        return JsonData.ok(videoInfoService.add(videoInfoVo));
    }

    @ApiOperation("通过id获取介绍和视频列表")
    @ApiImplicitParam(name="videoInfoId",value = "动漫id",paramType = "path",required = true,dataType = "long")
    @GetMapping("/getDetailById/{videoInfoId}")
    public JsonData<VideoInfoDo> getDetailById(@PathVariable("videoInfoId") Long videoInfoId){
            return JsonData.ok(videoInfoService.getDetailById(videoInfoId));
    }





    @ApiOperation("查询动漫介绍列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="pageNum",value = "页数",paramType = "path",required = true,dataType = "int"),
            @ApiImplicitParam(name="pageSize",value = "页大小",paramType = "path",required = true,dataType = "int")
    })
    @GetMapping("/selectList/{pageNum}/{pageSize}")
    public JsonData<VideoInfo> selectList(@PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize")Integer pageSize){

        return JsonData.ok(videoInfoService.selectList(pageNum,pageSize));
    }

    @ApiOperation("订阅动漫")
    @ApiImplicitParams({
            @ApiImplicitParam(name="videoInfoId",value = "动漫id",paramType = "path",required = true,dataType = "long"),
            @ApiImplicitParam(name="userId",value = "用户id",paramType = "path",required = true,dataType = "long")
    })
    @GetMapping("/followVideo/{videoInfoId}/{userId}")
    public JsonData followVideo(Long videoInfoId,Long userId){
        notifyService.createSubscription(userId,videoInfoId,"anime","follow");
        return JsonData.ok();
    }


    @ApiOperation("添加最新集")
    @PostMapping("/addNewEpisode")
    public JsonData addNewEpisode(@Validated(AddGroup.class) VideoVo videoVo){
        videoService.addNewEpisode(videoVo);
        return JsonData.ok();
    }
}
