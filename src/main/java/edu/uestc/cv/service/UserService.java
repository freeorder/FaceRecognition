package edu.uestc.cv.service;

import edu.uestc.cv.entity.User;
import edu.uestc.cv.exception.NotExistException;
import edu.uestc.cv.exception.ParamException;
import edu.uestc.cv.util.PageUtil;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * create by HQC on 2020/10/14 22:43
 **/
public interface UserService {

    void addUser(String number, String username, String password, String createTeacherId, Boolean male,String type, String role) throws Exception;

    //void registerUser(String username, String password) throws Exception;

    void updateUser(String username, String password, List<String> roles) throws Exception;

    PageUtil<User> findUserPage(int pageNo, int pageSize, String sortName, String sortOrder, String field, String value, int queryMethod);

    List<User> findUserByRole(String role, String sortName, String sortOrder, String field, String value, int queryMethod) throws ParamException;

    void deleteUser(String number) throws NotExistException, ParamException;

    void deleteUserRole(String number, String roleName) throws NotExistException, ParamException;

    void addUserRole(String number, String roleName) throws Exception;

    void changePassword(String oldPassword, String newPassword) throws Exception;

    void changeInformation(String number, String Password, String username, Boolean male) throws Exception;

    void updateUserStatus(String username, boolean isAccept) throws Exception;

    void importStudents(String filePath, MultipartFile file, String createTeacherId) throws Exception;

    PageUtil<User> findUnauthorizedUserPage(int pageNo, int pageSize, String sortName, String sortOrder, String field, String value, int queryMethod);

    List<String> authorizeUserByBatch(List<User> users, String roleName);




}
