package edu.uestc.cv.service;

import edu.uestc.cv.entity.Picture;
import edu.uestc.cv.entity.PictureUploadResult;
import edu.uestc.cv.exception.NotExistException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

/**
 * create by HQC on 2020/10/20 19:40
 **/
public interface PictureService {
    //员工照片识别
    Picture pictureRecognition(MultipartFile file);
    Picture pictureFileRecognition(File file);
    //mock上传自己的图片文件
    String upload(String id, MultipartFile file);
    //上传待识别的照片
    PictureUploadResult upRecognitonPicture(MultipartFile file);

    //查看图片文件
    List<String> checkFile(String id, String dirName) throws NotExistException;

    //删除图片文件
    String delFile(String id,String dirName);

    //下载图片文件
    File download(String dirName) throws Exception;
}
