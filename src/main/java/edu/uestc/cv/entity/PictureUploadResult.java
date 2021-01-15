package edu.uestc.cv.entity;

/**
 * create by HQC on 2020/12/3 23:13
 **/

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * 图片上传的类，该类图片需暂存用于图片的识别
 *
 *  {"error":0,
 *  "url":"图片的保存路径",
 *  "width":图片的宽度,
 *  "height":图片的高度}
 * @author HQC
 *
 */
@Document(collection = "PictureUploadResult")
public class PictureUploadResult {
    private String id;
    private Integer error=0;		//0表示无异常，1代表异常
    private String url;
    private String pictureName;
    private String pictureType;
    private String width;
    private String height;
    private Integer state; // 0 表示未识别，1代表已识别
    private Date RecognitionTime;
    private String staffName;//识别后返回识别的员工姓名
    private String label;//识别后返回的照片标签

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getPictureName() {
        return pictureName;
    }

    public void setPictureName(String pictureName) {
        this.pictureName = pictureName;
    }

    public String getPictureType() {
        return pictureType;
    }

    public void setPictureType(String pictureType) {
        this.pictureType = pictureType;
    }

    public Integer getError() {
        return error;
    }

    public void setError(Integer error) {
        this.error = error;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Date getRecognitionTime() {
        return RecognitionTime;
    }

    public void setRecognitionTime(Date recognitionTime) {
        RecognitionTime = recognitionTime;
    }
}
