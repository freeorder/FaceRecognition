package edu.uestc.cv.service.impl;

import edu.uestc.cv.constant.PasswordConstant;
import edu.uestc.cv.constant.RegexConstant;
import edu.uestc.cv.constant.RoleConstant;
import edu.uestc.cv.constant.StatusConstant;
import edu.uestc.cv.dao.RoleDAO;
import edu.uestc.cv.dao.UserDAO;
import edu.uestc.cv.entity.Role;
import edu.uestc.cv.entity.User;
import edu.uestc.cv.exception.NotExistException;
import edu.uestc.cv.exception.ParamException;
import edu.uestc.cv.service.AuthorityService;
import edu.uestc.cv.service.UserService;
import edu.uestc.cv.util.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * create by HQC on 2020/10/14 22:45
 **/
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserDAO userDao;
    @Autowired
    private RoleDAO roleDao;
    @Autowired
    AuthorityService authorityService;


    @Override
    public void addUser(String number, String username, String password, String createTeacherId, Boolean male,String type, String role) throws Exception{
        Set<Role> set = new HashSet<>();
        /*List<String> rolesCanGive = authorityService.listUserRole();

        Role roleInDb = roleDao.findRoleByName(role);
        if(rolesCanGive.contains(roleInDb.getRoleName())) {
            set.add(roleInDb);
        } else {
            throw new Exception("当前用户的权限不足,不能授予角色:" + roleInDb.getRoleName());
        }*/
        if( !RegexConstant.USER_NUMBER_PATTERN.matcher(number).matches()) {
            throw new Exception("用户编号不符合要求,必须是6-16位的正数");
        }
        if( !RegexConstant.USERNAME_PATTERN.matcher(username).matches()) {
            throw new Exception("用户名不符合要求,必须是2-20位汉字/字母或数字的组合");
        }
        if(!RegexConstant.PASSWORD_PATTERN.matcher(password).matches()) {
            throw new Exception("密码不符合要求,必须是6-16位数字/字母和特殊字符的组合,特殊字符只能在!@#$%^&*?,.中选取");
        }
        User user = new User(number,username, password, male,type,createTeacherId, RandomUtil.getStringRandom(PasswordConstant.SALT_LENGTH), set, StatusConstant.ACCEPT, new Date(), new Date());
        setTypeByRole(user);
        userDao.addUser(user);
    }

//    @Override
//    public void registerUser(String username, String password) throws Exception {
//
//        Role role;
//        try {
//            role = roleDao.findRoleByName(RoleConstant.USER);
//        } catch (NotExistException e) {
//            throw new Exception("未检测到普通用户角色，请联系管理员添加普通用户角色!");
//        }
//        Set<Role> set = new HashSet<>();
//        set.add(role);
//        User user = new User(username, password, RandomUtil.getStringRandom(PasswordConstant.SALT_LENGTH), set, StatusConstant.ACCEPT, new Date(), new Date());
//        userDao.addUser(user);
//    }

    @Override
    public void updateUser(String username, String password, List<String> roles) throws Exception {
        User user = userDao.findUserByUsername(username);
        if (password != null&&!password.equals("")) {
            if(!RegexConstant.PASSWORD_PATTERN.matcher(password).matches()) {
                throw new Exception("密码不符合要求,必须是6-16位数字/字母和特殊字符的组合,特殊字符只能在!@#$%^&*?,.中选取");
            }
            user.setPassword(password);
            user.setPassword(EncodeUserPassword.encode(user));
        }

        userDao.updateUser(user);
    }

    @Override
    public PageUtil<User> findUserPage(int pageNo, int pageSize, String sortName, String sortOrder, String field, String value, int queryMethod) {
        Subject currentUser = SecurityUtils.getSubject();
        User user = (User) currentUser.getPrincipal();
        return userDao.findPage(pageNo, pageSize, sortName, sortOrder, field, value, queryMethod, user);
    }

    @Override
    public List<User> findUserByRole(String role, String sortName, String sortOrder, String field, String value, int queryMethod) throws ParamException {
        Subject currentUser = SecurityUtils.getSubject();
        User user = (User) currentUser.getPrincipal();
        if (RoleJudge.isSchoolAdmin(user)) {
            if (role.equals("admin") || role.equals("companyAdmin")) {
                throw new ParamException("公司管理员不得查询系统管理员和其他公司管理员");
            }
        }
        return userDao.findUserByRole(role, sortName, sortOrder, field, value, queryMethod);
    }

    @Override
    public void deleteUser(String number) throws NotExistException, ParamException {
        User user = userDao.findUserByNumber(number);
        if (user == null) {
            throw new NotExistException("该账号不存在");
        } else {
            User currentUser = (User) SecurityUtils.getSubject().getPrincipal();
            if (!WhichRole.isSystemAdmin(currentUser)) {
                throw new ParamException("只有系统管理员才可删除学校教务员或系统管理员");
            }
        }
        userDao.deleteUser(user);
    }

    @Override
    public void deleteUserRole(String number, String roleName) throws NotExistException, ParamException {
        User user = userDao.findUserByNumber(number);
        User currentUser = (User) SecurityUtils.getSubject().getPrincipal();
        if (user==null) {
            throw new NotExistException("该账号不存在");
        } else if (WhichRole.isSystemAdmin(user) || WhichRole.isSchoolAdmin(user)) {
            if (!WhichRole.isSystemAdmin(currentUser)) {
                throw new ParamException("只有系统管理员才可删除学校教务员或者管理员的角色");
            }
        } else if(WhichRole.isTeacher(user) || WhichRole.isStudent(user)) {
            if(!WhichRole.isSchoolAdmin(currentUser)) {
                throw new ParamException("只有学校教务员才可删除教师或者学生的角色");
            }
        }
        User role_temp = userDao.findUserRoleByName(number, roleName);
        if (role_temp == null){
            throw new NotExistException("该账号没有此角色");
        } else {
            Role role = roleDao.findRoleByName(roleName);
            user.getRole().remove(role);
            userDao.updataUserRole(user);
        }
    }

    @Override
    public void addUserRole(String number, String roleName) throws Exception {
        User user = userDao.findUserByNumber(number);
        if (user==null){
            throw new NotExistException("该账号不存在");
        }
        User role_temp = userDao.findUserRoleByName(number, roleName);
        if (role_temp!=null){
            throw new NotExistException("该账号已有此角色");
        }else{
            Role role = roleDao.findRoleByName(roleName);
            List<String> rolesCanGive = authorityService.listUserRole();
            if(rolesCanGive.contains(role.getRoleName())) {
                LinkedHashSet<Role> newRole = new LinkedHashSet<>();
                newRole.add(role);
                user.setRole(newRole);
                setTypeByRole(user);
                userDao.updataUserRole(user);
            } else {
                throw new Exception("当前用户的权限不能授予此角色:" + roleName);
            }
        }
    }
    private void setTypeByRole(User user) {
        Set<Role> roles = user.getRole();
        if (roles != null && !roles.isEmpty()) {
            if(roles.contains(new Role(RoleConstant.ADMIN_SYSTEM))) {
                user.setType("3");
            } else if (roles.contains(new Role(RoleConstant.ADMIN_COMPANY))) {
                user.setType("2");
            } else if(roles.contains(new Role(RoleConstant.TEACHER))) {
                user.setType("1");
            } else {
                user.setType("0");
            }
        }
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) throws Exception {
        if (oldPassword.equals(newPassword)) {
            throw new Exception("新旧密码不能相同!");
        }
        if(!RegexConstant.PASSWORD_PATTERN.matcher(newPassword).matches()) {
            throw new Exception("密码不符合要求,必须是6-16位数字/字母和特殊字符的组合,特殊字符只能在!@#$%^&*?,.中选取");
        }

        Subject currentUser = SecurityUtils.getSubject();
        User user = (User) currentUser.getPrincipal();
        if (!user.getPassword().equals(MD5Util.MD5Value(MD5Util.MD5Value(oldPassword) + user.getSalt()))) {
            throw new Exception("密码错误!");
        } else {
            user.setPassword(newPassword);
            user.setPassword(EncodeUserPassword.encode(user));
            userDao.updateUser(user);
        }
    }

    //修改用户名或者性别
    @Override
    public void changeInformation(String number, String Password, String username, Boolean male) throws Exception {
        User user = userDao.findUserByNumber(number);
        if (!user.getPassword().equals(MD5Util.MD5Value(MD5Util.MD5Value(Password) + user.getSalt()))) {
            throw new Exception("密码错误!");
        } else {
            if( !RegexConstant.USERNAME_PATTERN.matcher(username).matches()) {
                throw new Exception("用户名不符合要求,必须是2-20位汉字/字母或数字的组合");
            }
            user.setUsername(username);
            user.setMale(male);
            userDao.updateUserInformation(user);
        }
    }

    @Override
    public void updateUserStatus(String number, boolean isAccept) throws Exception {
        if (number == null) {
            throw new Exception("账号不能为空!");
        }
        userDao.updateUserStatus(number, isAccept);
    }

    //hqc，该方法暂不需要
    @Override
    public void importStudents(String fileName, MultipartFile file, String createTeacherId) throws  Exception {
        /*List<User> studentsExcelDtos = StudentExcelUtil.getStudents(fileName, file,createTeacherId);
        Set<Role> role =new HashSet<>();
        StringBuilder errorMessage= new StringBuilder("以下信息导入失败！");
        boolean flag=false;//用于判断是否有错误数据
        for (User studentExcelDTO : studentsExcelDtos) {
            User user=userDao.findUserByNumber(studentExcelDTO.getNumber());
            if (user!=null) {
                errorMessage.append(studentExcelDTO.getNumber()).append(":账号重复\n");
                flag=true;
                continue;
            }
            if( !RegexConstant.USER_NUMBER_PATTERN.matcher(studentExcelDTO.getNumber()).matches()) {
                throw new Exception("用户编号不符合要求,必须是6-16位的正数");
            }
            if( !RegexConstant.USERNAME_PATTERN.matcher(studentExcelDTO.getUsername()).matches()) {
                errorMessage.append(studentExcelDTO.getNumber()).append(":用户名不符合要求,必须是2-20位汉字/字母或数字的组合");
                continue;
            }
            //默认密码为123456
            if(studentExcelDTO.getPassword()==null) {
                studentExcelDTO.setPassword("123456");
            }
            if(!RegexConstant.PASSWORD_PATTERN.matcher(studentExcelDTO.getPassword()).matches()) {
                errorMessage.append(studentExcelDTO.getNumber()).append(":密码不符合要求,必须是6-16位数字/字母和特殊字符的组合,特殊字符只能在!@#$%^&*?,.中选取");
                continue;
            }
            User users = new User(studentExcelDTO.getNumber(), studentExcelDTO.getUsername(), studentExcelDTO.getPassword(), studentExcelDTO.getMale(), studentExcelDTO.getType(),studentExcelDTO.getAdminId(), studentExcelDTO.getSalt(), role, StatusConstant.ACCEPT, new Date(), new Date());
            userDao.addUser(users);
        }
        if (flag)
            throw new Exception(errorMessage.toString());*/
    }

    @Override
    public PageUtil<User> findUnauthorizedUserPage(int pageNo, int pageSize, String sortName, String sortOrder, String field, String value, int queryMethod) {
        User currentUser = (User)SecurityUtils.getSubject().getPrincipal();
        List<User> users = userDao.findUnauthorizedUserPage(sortName, sortOrder, field, value, queryMethod, currentUser);
        return new PageUtil<>(pageNo, pageSize, users);

    }

    @Override
    public List<String> authorizeUserByBatch(List<User> users, String roleName) {
        Role roleInDb = null;
        List<String> exceptions =  new ArrayList<>();
        Subject subject = SecurityUtils.getSubject();
        User currentUser = (User) subject.getPrincipal();
        if (currentUser == null || ! RoleJudge.isAdmin(currentUser)) {
            exceptions.add("当前用户的角色不支持批量授权");
            return exceptions;
        }
        try {
            roleInDb = roleDao.findRoleByName(roleName);
            switch (roleInDb.getRoleName()) {
                case RoleConstant.ADMIN_SYSTEM:
                case RoleConstant.ADMIN_COMPANY:
                    //系统管理员能批量授权的角色:系统管理员/学校管理员
                    if (!RoleJudge.isSystemAdmin(currentUser)) {
                        exceptions.add("您的角色权限不够,不能授予权限:" + roleInDb.getRoleName());
                    }
                    break;
                case RoleConstant.TEACHER:
                case RoleConstant.STAFF:
                    //学校管理员能批量授权的角色:教师/学生
                    if (!RoleJudge.isSchoolAdmin(currentUser)) {
                        exceptions.add("您的角色权限不够,不能授予权限:" + roleInDb.getRoleName());
                    }
                    break;
                default:
                    exceptions.add("授权失败:" + roleInDb.getRoleName() +"不能被批量授权");
            }
        } catch (Exception e) {
            exceptions.add("授权失败:" + e.getMessage() + "\n");
        }


        for (User user:users) {
            User userInDb = userDao.findUserByNumber(user.getNumber());
            if(userInDb!=null) {
                LinkedHashSet<Role> newRole = new LinkedHashSet<>();
                newRole.add(roleInDb);
                userInDb.setRole(newRole);
                setTypeByRole(userInDb);
                userDao.updataUserRole(userInDb);
            } else {
                exceptions.add("授权失败：编号：" + user.getNumber() + "的用户不存在,请检查用户信息");
            }

        }
        return exceptions;
    }


}
