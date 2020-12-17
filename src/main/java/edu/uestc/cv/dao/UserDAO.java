package edu.uestc.cv.dao;

import edu.uestc.cv.constant.StatusConstant;
import edu.uestc.cv.entity.Role;
import edu.uestc.cv.entity.User;
import edu.uestc.cv.exception.ExistException;
import edu.uestc.cv.util.EncodeUserPassword;
import edu.uestc.cv.util.PageUtil;
import edu.uestc.cv.util.QueryUtil;
import edu.uestc.cv.util.RoleJudge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * create by HQC on 2020/10/14 22:42
 **/
@Repository
public class UserDAO {

    @Autowired
    MongoTemplate mongoTemplate;

    public void addUser(User user) throws Exception {
        User userInDB;
//        try {
//            userInDB = this.findUserByUsername(user.getUsername());
//        } catch (NotExistException e) {
//            //不存在才是好消息
//        }
        userInDB=findUserByNumber(user.getNumber());
        if (userInDB != null) {
            throw new ExistException(user.getNumber() + ":工号重复\n");
        }
        user.setPassword(EncodeUserPassword.encode(user));
        mongoTemplate.insert(user);
    }

    public void deleteUser(User user) {
        Query query = new Query(Criteria.where("_id").is(user.getId()));
        mongoTemplate.remove(query, User.class);
    }

    public User findUserByUsername(String username)  {
        Query query = new Query();
        Criteria criteria = Criteria.where("username").is(username);
        query.addCriteria(criteria);
        User user = mongoTemplate.findOne(query, User.class);
//        if (user == null) {
//            throw new NotExistException("用户不存在");
//        }
        return user;
    }

    public void updateUser(User user) {
        Update update = new Update();
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(user.getId()));
        if (user.getPassword() != null)
            //密码需要前置处理，此处是直接存储加密后的
            update.set("password", user.getPassword());
        update.set("gmtModified", new Date());
        mongoTemplate.updateFirst(query, update, User.class);
    }

    public PageUtil<User> findPage(int pageNo, int pageSize, String sortName, String sortOrder,
                                   String field, String value, int queryMethod, User user) {
        Query query = QueryUtil.getPageQueryBothFuzzyAndExactMatch(field, value, queryMethod);
        List<String> type = new ArrayList<>();
        if(RoleJudge.isSystemAdmin(user)) {
            type.add("2");
            type.add("3");
        }
        if(RoleJudge.isSchoolAdmin(user)){
            type.add("0");
            type.add("1");
        }
        query.addCriteria(Criteria.where("type").in(type));
        long temp = mongoTemplate.count(query, User.class);
        query.with(new Sort(Sort.Direction.fromString(sortOrder), sortName));
        query.skip((pageNo - 1) * pageSize);
        query.limit(pageSize);
        return new PageUtil<>(pageNo, pageSize, temp, mongoTemplate.find(query, User.class));
    }

    public List<User> findUserByRole(String role, String sortName, String sortOrder, String field, String value, int queryMethod) {
        Query query = QueryUtil.getPageQueryBothFuzzyAndExactMatch(field, value, queryMethod);
        query.with(new Sort(Sort.Direction.fromString(sortOrder), sortName));
        List<User> allUser = mongoTemplate.find(query, User.class);
        List<User> usersInRole = new ArrayList<>();

        for (User u : allUser) {
            if (u.getRole().contains(new Role(role))) {
                usersInRole.add(u);
            }
        }

        return usersInRole;
    }

    public void updateUserStatus(String number, boolean isAccept) {
        Query query = new Query();
        Update update = new Update();
        query.addCriteria(Criteria.where("username").is(number));
        if (isAccept)
            update.set("status", StatusConstant.ACCEPT);
        else
            update.set("status", StatusConstant.REJECT);
        mongoTemplate.updateFirst(query, update, User.class);
    }

    public User findUserByNumber(String number) {
        Query query = new Query();
        Criteria criteria = Criteria.where("number").is(number);
        query.addCriteria(criteria);

        return mongoTemplate.findOne(query, User.class);
    }

    public User findUserRoleByName(String number, String roleName) {
        Query query = new Query();
        Criteria criteria = Criteria.where("role.roleName").is(roleName).andOperator(Criteria.where("number").is(number));
        query.addCriteria(criteria);

        return mongoTemplate.findOne(query, User.class);
    }

    //修改用户名或者性别
    public void updateUserInformation(User user){
        Update update = new Update();
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(user.getId()));
        update.set("username", user.getUsername());
        update.set("male", user.getMale());
        update.set("gmtModified", new Date());
        mongoTemplate.updateFirst(query, update, User.class);
    }

    //更新用户角色
    public void updataUserRole(User user) {
        Update update = new Update();
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(user.getId()));
        update.set("role", user.getRole());
        update.set("type", user.getType());
        update.set("gmtModified", new Date());
        mongoTemplate.updateFirst(query, update, User.class);
    }

    public List<User> getAllUsers() {
        return mongoTemplate.find(new Query(), User.class);
    }

    public List<User> findUnauthorizedUserPage(String sortName, String sortOrder, String field, String value, int queryMethod, User user) {
        Query query = QueryUtil.getPageQueryBothFuzzyAndExactMatch(field, value, queryMethod);
        //role属性为空时，不能用null来判定
        query.addCriteria(Criteria.where("role").is(new ArrayList<>()));
        List<String> type = new ArrayList<>();
        if(RoleJudge.isSystemAdmin(user)) {
            type.add("2");
            type.add("3");
        }
        if(RoleJudge.isSchoolAdmin(user)){
            type.add("0");
            type.add("1");
        }
        query.addCriteria(Criteria.where("type").in(type));
//        query.addCriteria(Criteria.where("role").exists(false));
//        query.addCriteria(Criteria.where("role").type(10));
        query.with(new Sort(Sort.Direction.fromString(sortOrder), sortName));
        return mongoTemplate.find(query, User.class);
    }



}
