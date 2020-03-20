package com.zhu.rimxia.biz.controller;


import com.zhu.rimxia.biz.exception.BusinessException;
import com.zhu.rimxia.biz.exception.CommonErrorCode;
import com.zhu.rimxia.biz.mapper.FileUploadMapper;
import com.zhu.rimxia.biz.model.domain.FileUpload;
import com.zhu.rimxia.biz.util.id.IdUtil;
import io.swagger.annotations.Api;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.UUID;


@Api(description = "文件上传")
@RequestMapping("/file")
@RestController
public class FileUploadController {


    @Resource
    private FileUploadMapper fileUploadMapper;

    @PostMapping("/upload")
    public String uploadfile(@RequestParam("file") MultipartFile file) throws IOException {
        String fileType = file.getContentType();
        byte[] fileBytes = file.getBytes();
        FileUpload fileUpload = new FileUpload();
        fileUpload.setFileId(IdUtil.generateId());
        fileUpload.setFileType(file.getContentType());
        fileUpload.setFileName(file.getOriginalFilename());
        fileUpload.setContent(fileBytes);
        try{
            fileUploadMapper.insert(fileUpload);
        }catch (TransientDataAccessResourceException e){
            throw new BusinessException(CommonErrorCode.File_TOO_large,"64k");
        }

        return "http://127.0.0.1:8080/file/getImg/" + fileUpload.getFileId();
    }

    @GetMapping("/getImg/{fileId}")
    public void getfile(@PathVariable("fileId")Long fileId, HttpServletResponse response) throws IOException {
        FileUpload fileUpload = fileUploadMapper.selectByPrimaryKey(fileId);
        if(fileUpload==null){
            throw new BusinessException(CommonErrorCode.FILE_NO_EXIST,fileId);
        }
        response.setContentType(fileUpload.getFileType());
        response.setHeader("Content-disposition", "attachment; filename="+fileUpload.getFileName());
        ByteArrayInputStream in = new ByteArrayInputStream(fileUpload.getContent());
        int len;
        byte[] buf = new byte[1024];
        while ((len = in.read(buf,0,1024)) != -1)
            response.getOutputStream().write(buf, 0, len);
    }


}
