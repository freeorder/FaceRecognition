package edu.uestc.cv.entity;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Arrays;

/**
 * create by HQC on 2020/10/21 20:54
 **/
//za
public class Picture {

    private String id;

    private String number;
    //图片按照路径保存
    private String picturePath;
    //图片中识别人的名字
    private String name;
    //图片识别后返回的带人脸框识别结果
    private File file;
    //字节流形式图片文件
    private byte[] data;
    //图片名称（不包含类型）
    private String pictureName;
    //图片后缀类型
    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPictureName() {
        return pictureName;
    }

    public void setPictureName(String pictureName) {
        this.pictureName = pictureName;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Picture(String id, String number, String picturePath, String name, File file, byte[] data, String pictureName, String type) {
        this.id = id;
        this.number = number;
        this.picturePath = picturePath;
        this.name = name;
        this.file = file;
        this.data = data;
        this.pictureName = pictureName;
        this.type = type;
    }

    public Picture() {

    }

    @Override
    public String toString() {
        return "Picture{" +
                "id='" + id + '\'' +
                ", number='" + number + '\'' +
                ", picturePath='" + picturePath + '\'' +
                ", name='" + name + '\'' +
                ", file=" + file +
                ", data=" + Arrays.toString(data) +
                ", pictureName='" + pictureName + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
