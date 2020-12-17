package edu.uestc.cv.controller;

import com.alibaba.fastjson.JSONException;
import edu.uestc.cv.constant.*;
import edu.uestc.cv.entity.User;
import edu.uestc.cv.exception.NotExistException;
import edu.uestc.cv.service.UserService;
import edu.uestc.cv.util.PageUtil;
import edu.uestc.cv.util.ResultUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * create by HQC on 2020/10/14 22:41
 **/
@RestController
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    HttpSession session;

    //    @Value("#{configProperties['jdbc.excel.student.path']}")
    String studentExcelPath;

    Logger logger = LoggerFactory.getLogger(UserController.class);

//    @RequestMapping(value = "/register", method = RequestMethod.POST)
//    @ApiOperation("用户注册")
//    public ResultUtil<String> registerUser(@ApiParam(value = "用户名", required = true) @RequestParam() String username,
//                                           @ApiParam(value = "密码", required = true) @RequestParam() String password) {
//        try {
//            userService.registerUser(username, password);
//        } catch (Exception e) {
//            return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE, e.getMessage());
//        }
//        return new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS, "注册成功!");
//    }


    @RequestMapping(value = "/user/changePassword", method = RequestMethod.POST)
    @ApiOperation("用户修改自身密码")
    public ResultUtil<String> changePassword(@ApiParam(value = "旧密码", required = true) @RequestParam() String oldPassword,
                                             @ApiParam(value = "新密码", required = true) @RequestParam() String newPassword) {
        try {
            userService.changePassword(oldPassword, newPassword);
        } catch (NotExistException e) {
            return new ResultUtil<>(ResponseConstant.ResponseCode.EXIST_ERROR, "用户不存在");
        } catch (Exception e) {
            return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE, e.getMessage());
        }
        return new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS, "修改成功！");
    }

    //@RequiresRoles(value = {RoleConstant.ADMIN_SYSTEM, RoleConstant.ADMIN_COMPANY}, logical = Logical.OR)
    @RequestMapping(value = "/user/add", method = RequestMethod.POST)
    @ApiOperation("添加用户(限小批量添加)")
    public ResultUtil<String> addUser(@ApiParam(value = "工号", required = true) @RequestParam() String number,
                                      @ApiParam(value = "用户名", required = true) @RequestParam() String username,
                                      @ApiParam(value = "密码", required = true) @RequestParam() String password,
                                      @ApiParam(value = "性别", required = true) @RequestParam() Boolean male,
                                      @ApiParam(value = "类别(0代表待定, 1代表员工, 2代表公司管理员, 3代表系统管理员)", required = true) @RequestParam() String type,
                                      @ApiParam(value = "角色名", required = true) @RequestParam() String role) {
        String createTeacherId = (String) session.getAttribute(SessionConstant.TEACHER_ID);
        try {
            userService.addUser(number,username, password, createTeacherId,male,type,role);
        } catch (Exception e) {
            return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE, e.getMessage());
        }
        return new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS, "添加成功!");
    }

    //信息更改
    //@RequiresRoles(value = {RoleConstant.ADMIN_SYSTEM, RoleConstant.ADMIN_COMPANY, RoleConstant.TEACHER, RoleConstant.STAFF}, logical = Logical.OR)
    @RequestMapping(value = "/user/changeInformation", method = RequestMethod.POST)
    @ApiOperation("修改（自己的）用户名或者性别")
    public ResultUtil<String> changeInformation(@ApiParam(value = "工号", required = true) @RequestParam() String number,
                                                @ApiParam(value = "密码", required = true) @RequestParam() String Password,
                                                @ApiParam(value = "新用户名", required = false) @RequestParam() String username,
                                                @ApiParam(value = "新性别", required = false) @RequestParam() Boolean male) {
        try {
            userService.changeInformation(number, Password, username, male);
        } catch (NotExistException e) {
            return new ResultUtil<>(ResponseConstant.ResponseCode.EXIST_ERROR, "用户不存在");
        } catch (Exception e) {
            return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE, e.getMessage());
        }
        return new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS, "修改成功！");
    }


    //@RequiresRoles( value = {RoleConstant.ADMIN_SYSTEM, RoleConstant.ADMIN_COMPANY}, logical = Logical.OR)
    @RequestMapping(value = "/user/select", method = RequestMethod.POST)
    @ApiOperation("分页查询用户列表((只能看到自己能管理的用户))")
    public ResultUtil<PageUtil<User>> selectUser(@ApiParam(value = "页码", required = true) @RequestParam(defaultValue = "1") int page,
                                                 @ApiParam(value = "页面大小", required = true) @RequestParam(defaultValue = PageConstant.DEFAULT_PAGE_SIZE) int limit,
                                                 @ApiParam(value = "排序的字段,(默认为用户状态)") @RequestParam(required = false, defaultValue = "status") String sortName,
                                                 @ApiParam("排序字段的排序方式,只支持ASC、DESC") @RequestParam(required = false, defaultValue = SortConstant.DEFAULT_SORT_ORDER) String sortOrder,
                                                 @ApiParam(value = "查询字段", required = false) @RequestParam(defaultValue = "number") String field,
                                                 @ApiParam(value = "查询值", required = false) @RequestParam(defaultValue = "") String value,
                                                 @ApiParam(value = "查询方式(int型,0为模糊查询,其他为精确查询，默认值为0)", required = false) @RequestParam(defaultValue = "0") int queryMethod
    ) {
        if (limit > PageConstant.PAGE_MAX_SIZE) {
            return new ResultUtil<>(ResponseConstant.ResponseCode.PARAM_ERROR, "当前页请求的数据太大!", null);
        }
        if (!SortConstant.isValidateSortOrder(sortOrder)) {
            return new ResultUtil<>(ResponseConstant.ResponseCode.PARAM_ERROR, "排序参数错误!", null);
        }
        PageUtil<User> pageUtil = userService.findUserPage(page, limit, sortName, sortOrder, field, value, queryMethod);
        return new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS, "", pageUtil);
    }

    //@RequiresRoles( value = {RoleConstant.ADMIN_SYSTEM, RoleConstant.ADMIN_COMPANY}, logical = Logical.OR)
    @RequestMapping(value = "/user/selectByRole", method = RequestMethod.POST)
    @ApiOperation("根据角色搜索所有用户")
    public ResultUtil<List<User>> selectUserByRole(@ApiParam(value = "角色名(RoleConstant 之一)", required = true) @RequestParam(defaultValue = "teacher") String role,
                                                   @ApiParam(value = "排序的字段") @RequestParam(required = false, defaultValue = "number") String sortName,
                                                   @ApiParam("排序字段的排序方式,只支持ASC、DESC") @RequestParam(required = false, defaultValue = SortConstant.DEFAULT_SORT_ORDER) String sortOrder,
                                                   @ApiParam(value = "查询字段") @RequestParam(required = false, defaultValue = "number") String field,
                                                   @ApiParam(value = "查询值") @RequestParam(required = false, defaultValue = "") String value,
                                                   @ApiParam(value = "查询方式(int型,0为模糊查询,其他为精确查询，默认值为0)") @RequestParam(required = false, defaultValue = "0") int queryMethod) {
        try {
            return new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS, "", userService.findUserByRole(role, sortName, sortOrder, field, value, queryMethod));
        } catch (Exception e) {
            return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE, e.getMessage(), null);
        }
    }

    //@RequiresRoles( value = {RoleConstant.ADMIN_SYSTEM, RoleConstant.ADMIN_COMPANY}, logical = Logical.OR)
    @RequestMapping(value = "/user/delete", method = RequestMethod.DELETE)
    @ApiOperation("删除用户(限管理员使用)")
    public ResultUtil<String> deleteUser(@ApiParam(value = "工号", required = true) @RequestParam() String number) {
        try {
            userService.deleteUser(number);
        } catch (Exception e) {
            return new ResultUtil<>(ResponseConstant.ResponseCode.EXIST_ERROR, e.getMessage());
        }
        return new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS, "删除成功！");
    }


    //@RequiresRoles( value = {RoleConstant.ADMIN_SYSTEM, RoleConstant.ADMIN_COMPANY}, logical = Logical.OR)
    @RequestMapping(value = "/user/deleteRole", method = RequestMethod.DELETE)
    @ApiOperation("删除用户的角色(限管理员使用)")
    public ResultUtil<String> deleteUserRole(@ApiParam(value = "工号", required = true) @RequestParam() String number,
                                             @ApiParam(value = "角色名", required = true) @RequestParam() String roleName) {
        try {
            userService.deleteUserRole(number, roleName);
        } catch (Exception e) {
            return new ResultUtil<>(ResponseConstant.ResponseCode.EXIST_ERROR, e.getMessage());
        }
        return new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS, "删除成功！");
    }

    //@RequiresRoles( value = {RoleConstant.ADMIN_SYSTEM, RoleConstant.ADMIN_COMPANY}, logical = Logical.OR)
    @RequestMapping(value = "/user/addRole", method = RequestMethod.POST)
    @ApiOperation("设置用户角色(限管理员使用, 一个用户只能有一个角色, 原来用户没有角色时新增角色;原来用户有角色时, 新角色会替换原来的角色.)")
    public ResultUtil<String> addUserRole(@ApiParam(value = "工号", required = true) @RequestParam() String number,
                                          @ApiParam(value = "角色名", required = true) @RequestParam() String roleName) {
        try {
            userService.addUserRole(number, roleName);
        } catch (Exception e) {
            return new ResultUtil<>(ResponseConstant.ResponseCode.EXIST_ERROR, e.getMessage());
        }
        return new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS, "添加成功！");
    }
/*

    @RequiresRoles( value = {RoleConstant.ADMIN_COMPANY}, logical = Logical.OR)
    @RequestMapping(value = "/auth/UploadData",method = RequestMethod.POST)
    @ApiOperation("导入员工信息(仅限schoolAdmin)")
    public ResultUtil<String> doUploadFile(
            @ApiParam(value = "上传文件流", required = true) @RequestParam("file") MultipartFile file) throws IOException {
        //判断文件是否为空
        if(file==null) return null;
        //获取文件名
        String name=file.getOriginalFilename();
        //进一步判断文件是否为空（即判断其大小是否为0或其名称是否为null）
        long size=file.getSize();
        if(name==null || ("").equals(name) && size==0) return null;
        String createTeacherId = (String) session.getAttribute(SessionConstant.TEACHER_ID);
        try {
            userService.importStudents(name,file,createTeacherId);
        } catch (Exception e) {
            logger.info("管理(number:{})导入员工信息失败:{}", session.getAttribute(SessionConstant.TEACHER_NUMBER),
                    e.toString());
            return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE, e.toString());
        }
        logger.info("管理(number:{})导入员工信息成功", session.getAttribute(SessionConstant.TEACHER_NUMBER));
        return new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS, "导入成功!", null);
    }
*/


    //@RequiresRoles( value = {RoleConstant.ADMIN_SYSTEM, RoleConstant.ADMIN_COMPANY}, logical = Logical.OR)
    @RequestMapping(value = "/user/selectUnauthorizedUser", method = RequestMethod.POST)
    @ApiOperation("分页查询未分配角色的用户(只能看到自己能管理的用户)")
    public ResultUtil<PageUtil<User>> selectUnauthorizedUser(@ApiParam(value = "页码", required = true) @RequestParam(defaultValue = "1") int page,
                                                             @ApiParam(value = "页面大小", required = true) @RequestParam(defaultValue = PageConstant.DEFAULT_PAGE_SIZE) int limit,
                                                             @ApiParam(value = "排序的字段") @RequestParam(defaultValue = "number") String sortName,
                                                             @ApiParam("排序字段的排序方式,只支持ASC、DESC") @RequestParam(defaultValue = SortConstant.DEFAULT_SORT_ORDER) String sortOrder,
                                                             @ApiParam(value = "查询字段", required = false) @RequestParam(defaultValue = "type") String field,
                                                             @ApiParam(value = "查询值(查询字段为type时,查询值只能为0～3的整数, 分别代表:0代表待定, 1代表学生, 2代表学校管理员, 3代表系统管理员)", required = false) @RequestParam(defaultValue = "", required = false) String value,
                                                             @ApiParam(value = "查询方式(int型,0为模糊查询,其他为精确查询，默认值为0)", required = false) @RequestParam(defaultValue = "0") int queryMethod) {

        if (limit > PageConstant.PAGE_MAX_SIZE) {
            return new ResultUtil<>(ResponseConstant.ResponseCode.PARAM_ERROR, "当前页请求的数据太大!", null);
        }
        if (!SortConstant.isValidateSortOrder(sortOrder)) {
            return new ResultUtil<>(ResponseConstant.ResponseCode.PARAM_ERROR, "排序参数错误!", null);
        }

        //service
        return new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS, "", userService.findUnauthorizedUserPage(page, limit, sortName, sortOrder, field, value, queryMethod));
    }

    //@RequiresRoles( value = {RoleConstant.ADMIN_SYSTEM, RoleConstant.ADMIN_COMPANY}, logical = Logical.OR)
    @RequestMapping(value = "/user/authorizeUserByBatch", method = RequestMethod.POST)
    @ApiOperation("批量分配用户角色")
    public ResultUtil<List<String>> authorizeUserByBatch(@ApiParam(value = "需要授权的用户列表", required = true)@RequestBody List<User> users,
                                                         @ApiParam(value = "角色名(现在只能一个角色)", required = true)@RequestParam String roleName) {
        try {
//            List<String> list = JSONArray.parseArray(roleName, String.class);
            List<String> exceptions = userService.authorizeUserByBatch(users, roleName);
            if(exceptions==null||exceptions.size()==0) {
                return new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS, "批量授权成功", null);
            } else {
                return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE, "批量授权异常", exceptions);
            }
        } catch (JSONException e) {
            List<String> temp =new ArrayList<String>();
            temp.add("JSON格式错误："+ e.getMessage());
            return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE, "", temp);
        }


    }

}
