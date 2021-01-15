package edu.uestc.cv.controller;
/*

import edu.uestc.cv.constant.ResponseConstant;
import edu.uestc.cv.constant.SortConstant;
import edu.uestc.cv.entity.User;
import edu.uestc.cv.service.UserService;
import edu.uestc.cv.util.ResultUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

*/
/**
 * create by HQC on 2020/12/16 23:10
 **//*

@RestController
public class StaffController {

    @Autowired
    UserService userService;

    //返回一个可以获取所有工人的接口（包括工人姓名，当日是否考勤，如果考勤了则返回考勤时间）
    @RequestMapping(value = "/user/select", method = RequestMethod.POST)
    @ApiOperation("所有工人信息和当日考勤信息")
    public ResultUtil<List<User>> selectAllStaffAndAttendance(@ApiParam(value = "角色名(RoleConstant 之一)", required = true) @RequestParam(defaultValue = "staff") String role,
                                                   @ApiParam(value = "排序的字段") @RequestParam(required = false, defaultValue = "number") String sortName,
                                                   @ApiParam("排序字段的排序方式,只支持ASC、DESC") @RequestParam(required = false, defaultValue = SortConstant.DEFAULT_SORT_ORDER) String sortOrder,
                                                   @ApiParam(value = "查询字段") @RequestParam(required = false, defaultValue = "number") String field,
                                                   @ApiParam(value = "查询值") @RequestParam(required = false, defaultValue = "") String value,
                                                   @ApiParam(value = "查询方式(int型,0为模糊查询,其他为精确查询，默认值为0)") @RequestParam(required = false, defaultValue = "0") int queryMethod) {
        List<User> userList = new ArrayList<>();

        try {
            //userList = userService.findUserByRole(role, sortName, sortOrder, field, value, queryMethod);

            return new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS, "查找成功！");
        } catch (Exception e) {
            return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE,"查找失败！"+ e.getMessage(), null);
        }
    }
}
*/
