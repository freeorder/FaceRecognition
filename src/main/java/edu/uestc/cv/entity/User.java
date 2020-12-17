package edu.uestc.cv.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.uestc.cv.constant.DateConstant;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.File;
import java.util.Date;
import java.util.Set;

/**
 * create by HQC on 2020/10/14 16:32
 **/
@Document(collection = "user")
public class User {
    @Id
    @ApiModelProperty(hidden = true)
    private String id;//数据中的id是自动随机生成的，每个用户都有自己唯一的id
    private String number;//用户登陆编号
    private String username;//用户名
    @JsonIgnore
    private String password;//用户密码，6-16位数字、字母和特殊字符的组合

    private String adminId;//导入员工和待定数据时，用于显示到底是谁导入的

    @JsonIgnore
    private String salt;//盐值

    private Boolean male;


    //类别(0代表考勤管理者, 1代表工人 , 2代表公司管理员, 3代表系统管理员). 类别只是用于批量导入和方便批量授权, 与实际的权限管理中的角色并无强烈关系.
    private String type;

    private Set<Role> role;
    //权限 0通过 1审核中/未审核  2已拒绝
    private int status;

    @DateTimeFormat(pattern = DateConstant.DATETIME_PATTERN)
    @ApiModelProperty(hidden = true)
    private Date gmtCreate;//创建的时间

    @DateTimeFormat(pattern = DateConstant.DATETIME_PATTERN)
    @ApiModelProperty(hidden = true)
    private Date gmtModified;//最近修改的时间

    public User(){}

    public User(String number,String username, String password, Boolean male,String type,String adminId, String salt, Set<Role> role, int status, Date gmtCreate, Date gmtModified) {
        this.number=number;
        this.username = username;
        this.password = password;
        this.salt = salt;
        this.role = role;
        this.adminId = adminId;
        this.status = status;
        this.gmtCreate = gmtCreate;
        this.gmtModified = gmtModified;
        this.male=male;
        this.type=convertType(type);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
    public Set<Role> getRole() {
        return role;
    }

    public void setRole(Set<Role> role) {
        this.role = role;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public Boolean getMale() {
        return male;
    }

    public void setMale(Boolean male) {
        this.male = male;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = convertType(type);
    }

    /**
     * 用于限制type的值在0-3之间, 并默认为3
     * @param type
     * @return
     */
    private String convertType(String type) {
        //默认是工人类型
        if(null == type) {
            return "0";
        }
        switch (type) {
            case "1":
                return "1";
            case "2":
                return "2";
            case "3":
                return "3";
            default:
                return "0";
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", number='" + number + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", adminId='" + adminId + '\'' +
                ", salt='" + salt + '\'' +
                ", male=" + male +
                ", type='" + type + '\'' +
                ", role=" + role +
                ", status=" + status +
                ", gmtCreate=" + gmtCreate +
                ", gmtModified=" + gmtModified +
                '}';
    }
}
