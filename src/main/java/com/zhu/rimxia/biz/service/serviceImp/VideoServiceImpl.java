package com.zhu.rimxia.biz.service.serviceImp;

import com.zhu.rimxia.biz.common.CommandHelper;
import com.zhu.rimxia.biz.common.CommandUtil;
import com.zhu.rimxia.biz.common.ValidList;
import com.zhu.rimxia.biz.exception.BusinessException;
import com.zhu.rimxia.biz.exception.CommonErrorCode;
import com.zhu.rimxia.biz.mapper.VideoMapper;
import com.zhu.rimxia.biz.model.domain.Video;
import com.zhu.rimxia.biz.model.modelVo.VideoVo;
import com.zhu.rimxia.biz.service.NotifyService;
import com.zhu.rimxia.biz.service.VideoInfoService;
import com.zhu.rimxia.biz.service.VideoService;
import com.zhu.rimxia.biz.util.BeanUtil;
import com.zhu.rimxia.biz.util.id.IdUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class VideoServiceImpl implements VideoService {

    @Resource
    private VideoMapper videoMapper;

    @Resource
    private VideoInfoService videoInfoService;

    @Resource
    private NotifyService notifyService;



    @Override
    public List<Video> addList(List<VideoVo> videoVos) {
        List<Video> videos = new ArrayList<>();
        Date createDate = new Date();
        if(!videoVos.isEmpty()){
            Long videoInfoId = videoVos.get(0).getVideoInfoId();
            videoInfoService.getById(videoInfoId);
            Video video = new Video();
            for(VideoVo videoVo:videoVos){
                BeanUtil.copyProperties(videoVo,video);
                video.setVideoId(IdUtil.generateId());
                //固定第一个videoInfoId为所有对象的id，业务逻辑保证
                video.setVideoInfoId(videoInfoId);
                video.setCreateTime(createDate);
                videos.add(video);
            }
            videoMapper.insertList(videos);
        }

        return videos;
    }

    @Override
    public Video getById(Long videoId) {
        Video video = videoMapper.selectByPrimaryKey(videoId);
        if(video == null){
            throw  new BusinessException(CommonErrorCode.OBJECT_NOT_EXISTS,"video");
        }
        return video;
    }

    @Override
    public Video addNewEpisode(VideoVo videoVo) {
        long videoId = IdUtil.generateId();

        MultipartFile videoFile = videoVo.getVideoFile();
        String filename = videoFile.getOriginalFilename();
        System.out.println(filename + "-------->"+ videoFile.getSize());

        String path = "D:/FTP/"+videoId+"/";
        File dest = new File(path+filename);

        if(!dest.getParentFile().exists()){
            dest.getParentFile().mkdir();
        }

        String m3u8Filename = filename.split("\\.")[0]+".m3u8";

        try {
            videoFile.transferTo(dest);

            CommandUtil.exchangeToMp4(path+filename,
                    path+m3u8Filename);

        } catch (IOException e) {
            throw new BusinessException(CommonErrorCode.FILE_UPLOAD_ERROR);
        } catch (Exception e) {
            throw new BusinessException(CommonErrorCode.FILE_ENCODE_ERROR,videoId);
        }
       Video video =  Video.builder()
                .videoId(videoId)
                .url( "http://localhost:8080/video/"+m3u8Filename)
                .createTime(new Date())
                .sort(videoVo.getSort())
                .videoInfoId(videoVo.getVideoInfoId())
                .type(videoVo.getType())
                .videoName(videoVo.getVideoName())
                .build();
        videoMapper.insert(video);
        notifyService.createRemind(video.getVideoInfoId(),"anime",
                NotifyService.REMIND_TYPE_UPDATE,null,null);
        return video;
    }
}
