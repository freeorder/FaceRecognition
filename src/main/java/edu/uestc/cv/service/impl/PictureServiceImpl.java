package edu.uestc.cv.service.impl;

import edu.uestc.cv.entity.Picture;
import edu.uestc.cv.entity.PictureUploadResult;
import edu.uestc.cv.entity.User;
import edu.uestc.cv.exception.NotExistException;
import edu.uestc.cv.service.PictureService;
import org.apache.commons.io.FilenameUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.util.UUID;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * create by HQC on 2020/10/20 19:36
 **/
@Service
public class PictureServiceImpl implements PictureService {
    @Override
    public Picture pictureRecognition(MultipartFile file) {
        Picture picture = new Picture();
        byte[] data = new byte[0];
        try {
            data = file.getBytes();
        }catch (Exception e) {
            e.printStackTrace();
        }
        picture.setName("张三");
        //picture.setData(data);
        picture.setNumber("1234567");//模型完毕前暂设为定值
        return picture;
    }
    @Override
    public Picture pictureFileRecognition(File file) {//预览图片
        Picture picture = new Picture();

        try {
            //保存返回的识别照片
            String recognitionReturn = System.getProperty("rootPath") + "recognitionPictureSave"+File.separator;
            File directory = new File(recognitionReturn);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            //picture.setPicturePath(recognitionReturn+File.separator+file.getName());
            picture.setFile(file);
            picture.setName("张三");
            picture.setNumber("1234567");//模型完毕前暂设为定值
            //进行识别之后删除保存的照片
            /*if (file.exists()) {
                file.delete();
            }*/
        }catch (Exception e) {
            e.printStackTrace();
        }

        return picture;
    }

    @Override
    public String upload(String id, MultipartFile file) {//
        String usercodeSave = System.getProperty("rootPath") + "usercodeSave" + File.separator + id;
        File directory = new File(usercodeSave);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        try {
//            File userfile = new File(usercodeSave.getAbsolutePath() + "/" + file.getOriginalFilename());
//            file.transferTo(userfile);
//            userfile.setExecutable(true);
//            userfile.setWritable(true);
//            userfile.setReadable(true);

//            CommonsMultipartFile f = (CommonsMultipartFile) file;
            if (file.isEmpty()) {
                return "文件为空";
            }

            String filePath = FilenameUtils.concat(usercodeSave, file.getOriginalFilename());
            file.transferTo(new File(filePath));

//            f.transferTo(userfile);
//            byte[] b =f.getBytes();
//            OutputStream out=new FileOutputStream(userfile);
//            out.write(b);
//            out.flush();
//            out.close();

            return "保存成功";
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return "保存失败";
//            e.printStackTrace();
    }

    /**
     * HQC
     * 暂时只保存到本地，mongo中不保留待识别照片信息
     * */
    @Override
    public PictureUploadResult upRecognitonPicture(MultipartFile file) {
        PictureUploadResult pictureUploadResult=new PictureUploadResult();
        //1.判断是否为图片
        String fileName=file.getOriginalFilename();
        //不是图片
        if(!fileName.matches("^.*(png|jpg|gif|jpeg)$")){
            //不是图片类型
            pictureUploadResult.setError(1);
            return pictureUploadResult;
        }


        String recognitionPictureSave = System.getProperty("rootPath") + "recognitionPictureSave" + File.separator;
        File directory = new File(recognitionPictureSave);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        try {
//            File userfile = new File(usercodeSave.getAbsolutePath() + "/" + file.getOriginalFilename());
//            file.transferTo(userfile);
//            userfile.setExecutable(true);
//            userfile.setWritable(true);
//            userfile.setReadable(true);

//            CommonsMultipartFile f = (CommonsMultipartFile) file;
            if (file.isEmpty()) {
                pictureUploadResult.setError(1);
                return pictureUploadResult;
            }
            //防止图片上传量过大引起的重名问题
            String  uuidName=
                    UUID.randomUUID()
                            .toString().replace("-", "");
            String  randomNum=((int)(Math.random()*99999))+"";
            //获取文件后缀名
            String fileType=
                    fileName.substring(
                            fileName.lastIndexOf("."));
            //文件名拼接
            //String filePath = FilenameUtils.concat(recognitionPictureSave, file.getOriginalFilename());
            String pictureName = uuidName+randomNum;
            String fileSaveName =pictureName+fileType;
            String filePath = FilenameUtils.concat(recognitionPictureSave, fileSaveName);
            file.transferTo(new File(filePath));

//            f.transferTo(userfile);
//            byte[] b =f.getBytes();
//            OutputStream out=new FileOutputStream(userfile);
//            out.write(b);
//            out.flush();
//            out.close();
            pictureUploadResult.setUrl(recognitionPictureSave);
            pictureUploadResult.setPictureName(pictureName);
            pictureUploadResult.setPictureType(fileType.substring(fileType.lastIndexOf(".")+1));
            pictureUploadResult.setState(0);

            return pictureUploadResult;//保存成功
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        pictureUploadResult.setError(0);
        return pictureUploadResult;//保存失败
//            e.printStackTrace();
    }

    /**
    * HQC,暂未完成，该方法还需要新的PictureUploadResultDao用于保存图片信息到mongo
    * */
    //参考https://blog.csdn.net/weixin_42160445/article/details/82286892
    /*@Override
    public PictureUploadResult upRecognitonPicture(MultipartFile uploadFile) {
        PictureUploadResult pictureUploadResult=new PictureUploadResult();
        //1.判断是否为图片
        String fileName=uploadFile.getOriginalFilename();
        //不是图片
        if(!fileName.matches("^.*(png|jpg|gif|jpge)$")){
            //不是图片类型
            pictureUploadResult.setError(1);
            return pictureUploadResult;
        }
        //2.判断是否为恶意程序
        try {
            BufferedImage bufferedImage=
                    ImageIO.read(uploadFile.getInputStream());
            //2.1获取宽高
            int height=bufferedImage.getHeight();
            int width=bufferedImage.getWidth();

            if(height==0||width==0){
                //表示不是图片
                pictureUploadResult.setError(1);
                return pictureUploadResult;
            }

            //3.由于文件个数多,采用分文件存储
            //将系统格式的时间转换为文本（字符）格式
            *//*String dateDir=
                    new SimpleDateFormat("yyyy/MM/dd")
                            .format(new Date());*//*

            //生成对应的文件夹
            String filePath = System.getProperty("rootPath") + "recognitionPictureSave";
            String dirPath=filePath*//*+dateDir*//*;
            //判断是否存在
            File file=new File(dirPath);
            if(!file.exists()){
                //生成文件夹
                file.mkdirs();
            }
            //防止图片上传量过大引起的重名问题
            String  uuidName=
                    UUID.randomUUID()
                            .toString().replace("-", "");
            String  randomNum=((int)(Math.random()*99999))+"";
            //获取文件后缀名
            String fileType=
                    fileName.substring(
                            fileName.lastIndexOf("."));
            String prefix=fileName.substring(0, fileName.lastIndexOf("."));
            //路径拼接(文件真实的存储路径)
            String  fileDirPath=
                    dirPath+File.separator+prefix+uuidName+randomNum+fileType;

            //文件上传
            uploadFile.transferTo(new File(fileDirPath));

            //生成正确的页面回显信息
            pictureUploadResult.setHeight(height+"");
            pictureUploadResult.setWidth(width+"");
            *//**1.本地磁盘路径
             * 2.网络虚拟路径
             *//*
            //String  urlDir="http://image.jt.com/";//hqc,待定代码
            //String urlPath=urlDir+dateDir+File.separator+prefix+uuidName+randomNum+fileType;
            //pictureUploadResult.setUrl(urlPath);
            pictureUploadResult.setUrl(fileDirPath);

            //将picture信息保存到mongo
            pictureUploadResult.setState(0);//0代表还未调用识别接口


        } catch (IOException e) {
            e.printStackTrace();
        }

        return pictureUploadResult;
    }*/


    @Override
    public List<String> checkFile(String id, String dirName) throws NotExistException {
        File userfile =new File(System.getProperty("rootPath") + "usercodeSave/"+dirName);
        String[] list =userfile.list();
        if (list == null) {
            throw new NotExistException("文件不存在");
        }

        return new ArrayList<>(Arrays.asList(list));//hqc，有疑问
    }

    @Override
    public String delFile(String id ,String dirName){
        File userFile = new File(System.getProperty("rootPath")+"usercodeSave/"+id+"/"+dirName);
        if (userFile.exists()) {
            userFile.delete();
            return "删除成功";
        } else {
            return "该文件不存在";
        }
    }

//    @Override
//    public byte[] download(String id, String dirName) {
//        File userfile = new File(System.getProperty("rootPath") + "usercodeSave/" + id + "/" + dirName);
//        try {
//            InputStream inputStream = new FileInputStream(userfile);
//            byte[] b = null;
//            b=new byte[2048];
//            int byteread=0;
//            int bytesum=0;
//            while((byteread=inputStream.read(b))!=-1){
//                bytesum+=byteread;
//            }
//            return new byte[bytesum];
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//            return "未找到文件".getBytes();
//        } catch (IOException e) {
//            e.printStackTrace();
//            return "下载出错".getBytes();
//        }
//    }

    @Override
    public File download(String dirName) throws Exception {
        Subject currentUser = SecurityUtils.getSubject();
        User user = (User) currentUser.getPrincipal();
        String filePath = System.getProperty("rootPath") + "usercodeSave/" + user.getNumber() + "/" + dirName;
        File userFile = new File(filePath);
        if (!userFile.exists()) {
            throw new NotExistException("文件不存在");
        }

//        FileWriter fw = new FileWriter(userFile.getAbsoluteFile(), true);
//        BufferedWriter bw = new BufferedWriter(fw);
//        bw.write(fw.toString());
//        bw.close();

        return userFile;
    }


}
