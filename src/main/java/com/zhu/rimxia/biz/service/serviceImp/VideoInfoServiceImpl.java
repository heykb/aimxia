package com.zhu.rimxia.biz.service.serviceImp;

import com.github.pagehelper.PageHelper;
import com.zhu.rimxia.biz.exception.BusinessException;
import com.zhu.rimxia.biz.exception.CommonErrorCode;
import com.zhu.rimxia.biz.mapper.VideoInfoMapper;
import com.zhu.rimxia.biz.mapper.VideoMapper;
import com.zhu.rimxia.biz.model.domain.VideoInfo;
import com.zhu.rimxia.biz.model.modelDo.VideoInfoDo;
import com.zhu.rimxia.biz.model.modelVo.VideoInfoVo;
import com.zhu.rimxia.biz.service.VideoInfoService;
import com.zhu.rimxia.biz.service.VideoService;
import com.zhu.rimxia.biz.util.BeanUtil;
import com.zhu.rimxia.biz.util.id.IdUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class VideoInfoServiceImpl implements VideoInfoService {


    @Resource
    private VideoInfoMapper videoInfoMapper;


    @Resource
    private VideoService videoService;
    /**
     * 添加视频介绍
     * @param videoInfoVo
     * @return
     */
    @Override
    public VideoInfo add(VideoInfoVo videoInfoVo) {

        Date now = new Date();
        VideoInfo videoInfo = new VideoInfo();
        BeanUtil.copyPropertiesIgnoreNull(videoInfoVo,videoInfo);
        Long videoInfoId = IdUtil.generateId();
        videoInfo.setVideoInfoId(videoInfoId);
        videoInfo.setCreateTime(now);
        videoInfo.setUpdateTime(now);
        videoInfo.setNewEpisode(0);
        videoInfoMapper.insert(videoInfo);

//        if(videoInfoVo.getVideos() != null && !videoInfoVo.getVideos().isEmpty()){
//           //  System.out.println("fdfdfdfdfdfdfdf");
//            videoInfoVo.getVideos().get(0).setVideoInfoId(videoInfoId);
//            videoService.addList(videoInfoVo.getVideos());
//        }

        return videoInfo;
    }

    @Override
    public VideoInfo getById(Long videoInfoId) {
        VideoInfo videoInfo = videoInfoMapper.selectByPrimaryKey(videoInfoId);
        if(videoInfo == null){
            throw new BusinessException(CommonErrorCode.OBJECT_NOT_EXISTS,"VideoInfo");
        }
        return videoInfo;
    }

    @Override
    public VideoInfoDo getDetailById(Long videoInfoId) {
        VideoInfoDo videoInfoDo = videoInfoMapper.getDetailById(videoInfoId);
        if(videoInfoDo == null){
            throw new BusinessException(CommonErrorCode.OBJECT_NOT_EXISTS,"VideoInfo");
        }
        return videoInfoDo;
    }

    @Override
    public List<VideoInfo> selectList(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        PageHelper.orderBy("update_time desc");
        return videoInfoMapper.selectAll();
    }
}
