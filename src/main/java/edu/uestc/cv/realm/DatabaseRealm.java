package edu.uestc.cv.realm;

import edu.uestc.cv.dao.UserDAO;
import edu.uestc.cv.entity.Permission;
import edu.uestc.cv.entity.Role;
import edu.uestc.cv.entity.User;
import edu.uestc.cv.util.MD5Util;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

public class DatabaseRealm extends AuthorizingRealm {
    @Autowired
    UserDAO userDAO;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //能进入到这里，表示账号已经通过验证了
        User user = (User) principalCollection.getPrimaryPrincipal();
        //通过DAO获取角色和权限
        Set<Role> roles;
        Set<String> roleForAuth = new HashSet<>();
        Set<String> permissionForAuth = new HashSet<>();
        roles = user.getRole();
            if (roles != null) {
                for (Role role : roles) {
                    roleForAuth.add(role.getRoleName());
                    for (Permission permission : role.getPermissions()) {
                        permissionForAuth.add(permission.getPermissionName());
                    }
                }
            }

        //授权对象
        SimpleAuthorizationInfo s = new SimpleAuthorizationInfo();
        //把通过service获取到的角色和权限放进去
        s.setStringPermissions(permissionForAuth);
        s.setRoles(roleForAuth);
        return s;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String username = String.valueOf(token.getPrincipal());
        String password = new String((char[]) token.getCredentials());
        //获取数据库中的密码
        User user = userDAO.findUserByNumber(username);

        //user=userDAO.findUserByUsername(username);
        if (null==user)
            throw new AuthenticationException();
        if (user.getStatus() != 0) {
            throw new DisabledAccountException();
        }


        String passwordInDB = user.getPassword();
        String salt = user.getSalt();
        String passwordEncoded = MD5Util.MD5Value(MD5Util.MD5Value(password) + salt);

        //如果为空就是账号不存在，如果不相同就是密码错误，但是都抛出AuthenticationException，而不是抛出具体错误原因，免得给破解者提供帮助信息
        if (!passwordEncoded.equals(passwordInDB))
            throw new AuthenticationException();

        //认证信息里存放账号密码, getName() 是当前Realm的继承方法,通常返回当前类名 :databaseRealm
        SimpleAuthenticationInfo a = new SimpleAuthenticationInfo(user, password, getName());
        return a;
    }
}
