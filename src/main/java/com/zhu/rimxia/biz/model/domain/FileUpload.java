package com.zhu.rimxia.biz.model.domain;

import javax.persistence.*;

@Table(name = "file_upload")
public class FileUpload {
    /**
     * 文件id
     */
    @Id
    @Column(name = "file_id")
    private Long fileId;

    /**
     * 文件名
     */
    @Column(name = "file_name")
    private String fileName;

    /**
     * 文件类型
     */
    @Column(name = "file_type")
    private String fileType;

    /**
     * 文件内容二进制
     */
    private byte[] content;

    /**
     * 获取文件id
     *
     * @return file_id - 文件id
     */
    public Long getFileId() {
        return fileId;
    }

    /**
     * 设置文件id
     *
     * @param fileId 文件id
     */
    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    /**
     * 获取文件名
     *
     * @return file_name - 文件名
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * 设置文件名
     *
     * @param fileName 文件名
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * 获取文件类型
     *
     * @return file_type - 文件类型
     */
    public String getFileType() {
        return fileType;
    }

    /**
     * 设置文件类型
     *
     * @param fileType 文件类型
     */
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    /**
     * 获取文件内容二进制
     *
     * @return content - 文件内容二进制
     */
    public byte[] getContent() {
        return content;
    }

    /**
     * 设置文件内容二进制
     *
     * @param content 文件内容二进制
     */
    public void setContent(byte[] content) {
        this.content = content;
    }
}