package edu.uestc.cv.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import edu.uestc.cv.constant.PictureConstant;
import edu.uestc.cv.constant.ResponseConstant;
import edu.uestc.cv.constant.RoleConstant;
import edu.uestc.cv.entity.Picture;
import edu.uestc.cv.entity.PictureUploadResult;
import edu.uestc.cv.service.PictureService;
import edu.uestc.cv.util.HttpUtil;
import edu.uestc.cv.util.PictureUtil;
import edu.uestc.cv.util.ResultUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.io.FileUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * create by HQC on 2020/10/20 19:07
 **/

//接口任务 1.接收前端图片上传并保存到数据库
@RestController
public class PictureController {
    @Autowired
    PictureService pictureService;

    //@RequiresRoles(value = {RoleConstant.ADMIN_COMPANY, RoleConstant.TEACHER,RoleConstant.STAFF},logical = Logical.OR)
    @RequestMapping(value = "/picture/uploadPictureFile",method = RequestMethod.POST)
    @ApiOperation("员工上传照片文件")//hqc
    public ResultUtil<String> uploadPictureFile(@ApiParam(value ="员工id",required = true)@RequestParam() String id,
                                         @ApiParam(value = "文件",required = true)@RequestParam("file") MultipartFile file){

        if (file.getSize()> PictureConstant.pictureSize){
            return new ResultUtil<String>(ResponseConstant.ResponseCode.FAILURE,"图片太大",null);
        }
        String res = pictureService.upload(id, file);
        if (res=="保存成功"){
            return new ResultUtil<String>(ResponseConstant.ResponseCode.SUCCESS,"保存成功",null);
        }else {
            return new ResultUtil<String>(ResponseConstant.ResponseCode.FAILURE,"保存失败",null);
        }

    }

    @RequestMapping(value = "/picture/uploadRecognitionPicture",method = RequestMethod.POST)
    @ApiOperation("系统上传识别照片并进行识别")//系统上传一张待识别的照片，临时保存到数据库，返回该照片id，在前端调用识别接口之后删除该照片
    public ResultUtil<PictureUploadResult> uploadRecognitionPicture(
            @ApiParam(value = "文件",required = true)@RequestParam("file") MultipartFile file){

        if (file.getSize()> PictureConstant.pictureSize){
            return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE,"图片太大",null);
        }
        PictureUploadResult pictureUploadResult = pictureService.upRecognitonPicture(file);

       // String pictureFullName = pictureName+"."+pictureType;
        String pictureFullName = pictureUploadResult.getPictureName()+"."+pictureUploadResult.getPictureType();
        String savePath = PictureUtil.pictureSaveUrl + File.separator+pictureFullName;
        File savefile = new File(savePath);
        //进行人脸识别（未实装，返回固定值），然后删除暂存的识别图片
        Picture picture = pictureService.pictureFileRecognition(savefile,pictureFullName,savePath);

        pictureUploadResult.setStaffName(picture.getName());
        if (pictureUploadResult.getError()==0){//Error为0表示无异常，1表示有异常
            return new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS,"上传识别照片成功",pictureUploadResult);
        }else {
            return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE,"上传识别照片失败",pictureUploadResult);
        }

    }

    @RequestMapping(value = "/picture/RecognitionPicture",method = RequestMethod.GET)
    @ApiOperation("识别照片")//模型返回数据result形式 "{\"label\":\"867\",\"name\":\"Vladimir_Spidla\"}\n"
    public ResultUtil<String> RecognitionPicture(
            @ApiParam(value = "图片存储名称（由系统上传接口返回）",required = true)@RequestParam String pictureName,
            @ApiParam(value = "图片存储类型（由系统上传接口返回）",required = true)@RequestParam String pictureType){

        String result=null;
        String name = null;
        try {
            result = HttpUtil.sendGet3("http://localhost:5010/predict?path=upload/"+pictureName+"."+pictureType);
            String str = result.replaceAll("\\\\","");
            str = str.replaceAll("\n","");
            JSONObject obj= JSON.parseObject(str);
            name = obj.get("name").toString();
            /*int index = result.indexOf(":");
            index=result.indexOf(":", index+1);
            name = result.substring(index);//截取第二个：之后的字符 \"Vladimir_Spidla\"}\n"
            index = name.indexOf("\"");
            name = name.substring(index+1,name.indexOf("\"",index+1));*/


        }catch (Exception e){
            e.printStackTrace();
            return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE,"识别失败"+e.getMessage(),result);
        }
        if (result==null||result.length()<=0){//Error为0表示无异常，1表示有异常
            return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE,"识别失败!"+"http://localhost:5010/predict?path=upload/"+pictureName+"."+pictureType,result);
        }else {
            return new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS,"识别成功",name);
        }

    }


    //@RequiresRoles(value = {RoleConstant.ADMIN_COMPANY, RoleConstant.TEACHER,RoleConstant.STAFF},logical = Logical.OR)
       /* @RequestMapping(value = "/picture/PictureRecognition",method = RequestMethod.POST)
        @ApiOperation("员工照片识别")//传一张照片给模型识别(模型暂未实装)，识别成功返回圈定人脸的图片、员工姓名、员工id，失败返回“识别失败”
        public ResultUtil<Picture> PictureRecognition(@ApiParam(value = "文件",required = true)@RequestParam("file") MultipartFile file){

            if (file.getSize()> PictureConstant.pictureSize){
                return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE, "图片太大", null);
            }
            Picture picture = pictureService.pictureRecognition(file);
            //删除本地保存的识别照片
            if (picture.getName()!=null){
                return new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS,"识别成功",picture);
            }else {
                return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE,"识别失败",picture);
            }
    }*/

    /*@RequestMapping(value = "/picture/PreviewPictureRecordFile",method = RequestMethod.GET)
    @ApiOperation("预览识别照片结果")//传一张照片给模型识别(模型暂未实装)，识别成功返回圈定人脸的图片、员工姓名、员工id，失败返回“识别失败”
    public ResultUtil<String> PreviewPictureRecordFile(
            @ApiParam(value = "预览图片存储名称（由系统上传接口返回）",required = true)@RequestParam String pictureName,
            @ApiParam(value = "预览图片存储类型（由系统上传接口返回）",required = true)@RequestParam String pictureType,
            HttpServletResponse response
    ){
        Picture picture;
        try{
            String pictureFullName = pictureName+"."+pictureType;
            String savePath = PictureUtil.pictureSaveUrl + File.separator+pictureFullName;
            File file = new File(savePath);
            //进行人脸识别（未实装，返回固定值），然后删除暂存的识别图片
            //picture = pictureService.pictureFileRecognition(file);
            //InputStream inputStream = new BufferedInputStream(new FileInputStream(savePath));
            InputStream inputStream = new FileInputStream(file);
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read();
            inputStream.close();
            response.reset();
            //response.setHeader("content-Disposition","FileName="+ URLEncoder.encode(pictureFullName, StandardCharsets.UTF_8));
            response.setContentType("text/html; charset=UTF-8");
            if (pictureType.equals("jpg")||pictureType.equals("jpeg")){
                response.setHeader("content-type", MediaType.IMAGE_JPEG_VALUE);
            }
            else if (pictureType.equals("png")){
                response.setHeader("content-type", MediaType.IMAGE_PNG_VALUE);
            }
            else if (pictureType.equals("gif")){
                response.setHeader("content-type", MediaType.IMAGE_GIF_VALUE);
            }else{
                return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE,"识别失败");
            }

            //response.setHeader("Pragma", "no-cache");//不使用缓存

            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            toClient.write(buffer);
            toClient.flush();
            toClient.close();

        }catch (Exception e){
            return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE,"识别失败"+e.getMessage());
        }

        return new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS,"识别成功！");
    }*/


    @RequestMapping(value = "/picture/PreviewPictureRecordFile",method = RequestMethod.GET)
    @ApiOperation("预览识别后照片")//传一张照片给模型识别(模型暂未实装)，识别成功返回圈定人脸的图片、员工姓名、员工id，失败返回“识别失败”
    public ResultUtil<String> PreviewPictureRecordFile(
            @ApiParam(value = "预览图片存储名称（由系统上传接口返回）",required = true)@RequestParam String pictureName,
            @ApiParam(value = "预览图片存储类型（由系统上传接口返回）",required = true)@RequestParam String pictureType,
            HttpServletResponse response
    ){
        Picture picture;
        try{
            String pictureFullName = pictureName+"."+pictureType;
            String savePath = PictureUtil.pictureSaveUrl + File.separator+pictureFullName;
            File file = new File(savePath);
            //进行人脸识别（未实装，返回固定值），然后删除暂存的识别图片
            //picture = pictureService.pictureFileRecognition(file,pictureFullName,savePath);
            InputStream inputStream = new FileInputStream(file);
            byte[] bytes = null;
            OutputStream outputStream = response.getOutputStream();

            if (pictureType.equals("jpg")||pictureType.equals("jpeg")){
                response.setHeader("content-type", MediaType.IMAGE_JPEG_VALUE);
            } else if (pictureType.equals("png")){
                response.setHeader("content-type", MediaType.IMAGE_PNG_VALUE);
            } else if (pictureType.equals("gif")){
                response.setHeader("content-type", MediaType.IMAGE_GIF_VALUE);
            }else{
                return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE,"预览失败");
            }
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("expires", -1);
            response.setHeader("Content-Disposition", "inline");


            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            inputStream.close();
            outputStream.close();

        }catch (Exception e){
            return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE,"识别失败"+e.getMessage());
        }

        return new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS,"识别成功！");
    }

    //@RequiresRoles(value = {RoleConstant.ADMIN_COMPANY, RoleConstant.TEACHER,RoleConstant.STAFF},logical = Logical.OR)
    @RequestMapping(value = "/picture/checkPictureFile",method = RequestMethod.GET)
    @ApiOperation("员工查看图片文件目录")//hqc 以下代码需要上传
    public ResultUtil<List<String>> checkPictureFile(@ApiParam(value = "员工id",required = true) @RequestParam String id,
                                              @ApiParam(value = "文件夹名(usercodeSave之后的部分路径，若仅为工号路径可不填)")@RequestParam(required = false) String dirName){
        try {
            if (id.equals(dirName) || dirName == null) {
                List<String> list = pictureService.checkFile(id, id);
                return new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS,"保存成功",list);
            } else {
                List<String> list = pictureService.checkFile(id, dirName);
                return new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS,"保存成功",list);
            }
        } catch (Exception e) {
            return new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS, e.getMessage(),null);
        }

    }

   // @RequiresRoles(value = {RoleConstant.ADMIN_COMPANY, RoleConstant.TEACHER,RoleConstant.STAFF},logical = Logical.OR)
    @RequestMapping(value = "/picture/delFile",method = RequestMethod.POST)
    @ApiOperation("员工删除文件")//hqc
    public ResultUtil<String> delFile(@ApiParam(value = "学生id",required = true)@RequestParam String id,
                                      @ApiParam(value = "文件名（若不在学生根目录下，需要包含上级目录）",required = true)@RequestParam String dirName){
        String res = pictureService.delFile(id, dirName);
        if (res=="删除成功"){
            return new ResultUtil<String>(ResponseConstant.ResponseCode.SUCCESS,"删除成功",null);
        }else
            return new ResultUtil<String>(ResponseConstant.ResponseCode.FAILURE,"文件不存在",null);

    }

    //@RequiresRoles(value = {RoleConstant.STAFF},logical = Logical.OR)
    @RequestMapping(value = "/picture/downloadFile",method = RequestMethod.GET)
    @ApiOperation("员工下载文件(只能下载自己的文件)")
    public ResponseEntity<byte[]> downloadFile(@ApiParam(value = "文件名（若不在学生根目录下，需要包含上级目录） ",required = true) @RequestParam String dirName) {
        HttpHeaders headers = new HttpHeaders();
        try {
            File body = pictureService.download(dirName);
//            return new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS,"下载成功",body);
            headers.setContentDispositionFormData("attachment", body.getName());
            return new ResponseEntity<>(FileUtils.readFileToByteArray(body), headers, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("下载失败".getBytes(), headers, HttpStatus.CREATED);
        }
    }
}
