package edu.uestc.cv.entity;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Document(collection = "role")
public class Role {
    @Id
    @ApiModelProperty(hidden = true)
    private String id;

    private String roleName;

    private Set<Permission> permissions;

    public Role() {
    }

    public Role(String roleName) {
        this.roleName = roleName;
    }

    public Role(String id, String roleName, Set<Permission> permissions) {
        this.id = id;
        this.roleName = roleName;
        this.permissions = permissions;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    //重写equals方法，当roleName相同时，两个Role对象相同
    @Override
    public boolean equals(Object o) {
        if(this==o) {
            return true;
        }
        if(o instanceof Role) {
            Role role = (Role) o;
            return roleName.equals(role.getRoleName());
        }
        return false;
    }

    //重写hashCode方法，当roleName相同时，返回的hashCode相同
    @Override
    public int hashCode() {
        char[] charArr = roleName.toCharArray();
        int hash = 0;
        for(char c : charArr) {
            hash = hash * 131 + c;
        }
        return hash;
    }
}
