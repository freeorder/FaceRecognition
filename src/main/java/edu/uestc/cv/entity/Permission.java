package edu.uestc.cv.entity;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "permission")
public class Permission {
    @Id
    @ApiModelProperty(hidden = true)
    private String id;

    private String permissionName;

    public Permission() {
    }

    public Permission(String permissionName) {
        this.permissionName = permissionName;
    }

    public Permission(String id, String permissionName) {
        this.id = id;
        this.permissionName = permissionName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }


    //重写equals方法，当id相同时，两个permission对象相同
    @Override
    public boolean equals(Object o) {
        if(o instanceof Permission) {
            Permission permission = (Permission)o;
            return id.equals(permission.getId());
        }
        return false;
    }

    //重写hashCode方法，当id相同时，返回的hashCode相同
    @Override
    public int hashCode() {
        char[] charArr = id.toCharArray();
        int hash = 0;
        for(char c : charArr) {
            hash = hash * 131 + c;
        }
        return hash;
    }
}
