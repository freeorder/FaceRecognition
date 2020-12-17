package edu.uestc.cv.service;

import edu.uestc.cv.exception.ExistException;
import edu.uestc.cv.exception.NotExistException;

import java.util.List;

public interface AuthorityService {
    /**
     * @param permissionName
     * @return void
     * @author Jiechao
     * @date 19-3-27 下午3:51
     */
    void addPermission(String permissionName) throws ExistException;

    /**
     * @param roleName
     * @param permission
     * @return void
     * @author Jiechao
     * @date 19-3-27 下午4:13
     */
    void addRole(String roleName, List<String> permission) throws ExistException, NotExistException;

    void updateRole(String roleName, List<String> permissions) throws NotExistException;

    List<String> listAllPermission();

    List<String> listAllRole();

    List<String> listUserRole();

    void deleteRole(List<String> roleName) throws Exception;

    List<String> getRolePermisssions(String roleName) throws NotExistException;

    void delPermission(List<String> permissionName) throws NotExistException;
}
