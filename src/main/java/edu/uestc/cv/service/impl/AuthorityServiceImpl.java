package edu.uestc.cv.service.impl;

import edu.uestc.cv.constant.RoleConstant;
import edu.uestc.cv.dao.PermissionDAO;
import edu.uestc.cv.dao.RoleDAO;
import edu.uestc.cv.dao.UserDAO;
import edu.uestc.cv.entity.Permission;
import edu.uestc.cv.entity.Role;
import edu.uestc.cv.entity.User;
import edu.uestc.cv.exception.ExistException;
import edu.uestc.cv.exception.NotExistException;
import edu.uestc.cv.service.AuthorityService;
import edu.uestc.cv.util.RoleJudge;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuthorityServiceImpl implements AuthorityService {
    @Autowired
    PermissionDAO permissionDAO;
    @Autowired
    RoleDAO roleDAO;
    @Autowired
    UserDAO userDAO;

    @Override
    public void addPermission(String permissionName) throws ExistException {
        permissionDAO.addPermission(new Permission(permissionName));
    }

    @Override
    public void addRole(String roleName, List<String> permission) throws ExistException, NotExistException {
        Role role = new Role(roleName);
        Set<Permission> permissions = new HashSet<>();
        for (String name : permission) {
            permissions.add(permissionDAO.findPermissionByName(name));
        }
        role.setPermissions(permissions);
        roleDAO.addRole(role);
    }

    @Override
    public void updateRole(String roleName, List<String> permissions) throws NotExistException {
        Role role = roleDAO.findRoleByName(roleName);
        Set<Permission> permissionsInDB = new HashSet<>();
        for (String permission : permissions) {
            permissionsInDB.add(permissionDAO.findPermissionByName(permission));
        }
        role.setPermissions(permissionsInDB);
        roleDAO.updateRole(role);
    }

    @Override
    public List<String> listAllPermission() {
        return permissionDAO.getAllPermissions();
    }

    @Override
    public List<String> listAllRole() {
        return roleDAO.getAllRoles();
    }

    @Override
    public List<String> listUserRole() {
        List<String> rolesInDb=roleDAO.getAllRoles();
        List<String> rolesCanGive = new ArrayList<>();
        Subject subject = SecurityUtils.getSubject();
        User currentUser = (User) subject.getPrincipal();
        if(RoleJudge.isSystemAdmin(currentUser)) {
            //系统管理员能批量授权的角色:系统管理员和公司管理员
            if(rolesInDb.contains(RoleConstant.ADMIN_SYSTEM))
                rolesCanGive.add(RoleConstant.ADMIN_SYSTEM);
            if(rolesInDb.contains(RoleConstant.ADMIN_COMPANY))
                rolesCanGive.add(RoleConstant.ADMIN_COMPANY);
        }
        if(RoleJudge.isSchoolAdmin(currentUser)) {
            //学校管理员能批量授权的角色:教师和员工
            if(rolesInDb.contains(RoleConstant.TEACHER))
                rolesCanGive.add(RoleConstant.TEACHER);
            if(rolesInDb.contains(RoleConstant.STAFF))
                rolesCanGive.add(RoleConstant.STAFF);
        }
        return rolesCanGive;
    }

    @Override
    public void deleteRole(List<String> roleName) throws Exception {
        for (String name :roleName){
            Role role = roleDAO.findRoleByName(name);

            roleDAO.deleteRole(name);
            deleteUserRole(role);

        }

    }

    @Override
    public List<String> getRolePermisssions(String roleName) throws NotExistException{
        Role role =roleDAO.findRoleByName(roleName);
        Set<Permission> permissions= role.getPermissions();
        List<String> pers=new LinkedList<>();
        for(Permission permission:permissions){
            pers.add( permission.getPermissionName());
        }
        return pers;
    }

    @Override
    public void delPermission(List<String> permissionName) throws NotExistException {
        for (String permission : permissionName) {
            Permission permissionInDb = permissionDAO.findPermissionByName(permission);

            permissionDAO.deletePermission(new Permission(permission));
            //删除角色对应的权限
            List<String> ids = roleDAO.deleteRolePermission(permissionInDb);

            //更新用户的角色
            updateUsersRoleForPermissionChange(ids);
        }
    }

    private void updateUsersRoleForPermissionChange(List<String> ids) {
        List<Role> roles = new ArrayList<>();
        for (String id : ids) {
            try {
                Role role = roleDAO.findRoleById(id);
                roles.add(role);
            } catch (NotExistException e) {
                //没找到的role就跳过
            }
        }

        List<User> users = userDAO.getAllUsers();
        //查找所有user,然后依次修改role
        for(User user:users) {
            for(Role role:roles) {
                if(user.getRole().contains(role)) {
                    //role对象相同的判定依据是id，所以先删除再添加，就实现了角色的更新
                    user.getRole().remove(role);
                    user.getRole().add(role);
                    userDAO.updataUserRole(user);
                }
            }
        }
    }

    //删除role时，要把所有用到这个role的记录都去掉这个角色
    private void deleteUserRole(Role role) {
        //查找所有user,然后依次修改role
        List<User> users = userDAO.getAllUsers();
        for(User user:users) {
            if(user.getRole().contains(role)) {
                user.getRole().remove(role);
                userDAO.updataUserRole(user);
            }
        }

    }

}




