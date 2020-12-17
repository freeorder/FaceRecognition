package edu.uestc.cv.dao;

import edu.uestc.cv.entity.Permission;
import edu.uestc.cv.entity.Role;
import edu.uestc.cv.exception.ExistException;
import edu.uestc.cv.exception.NotExistException;
import edu.uestc.cv.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Repository
public class RoleDAO {
    @Autowired
    MongoTemplate mongoTemplate;

    public Role findRoleByName(String roleName) throws NotExistException {
        Query query = new Query();
        Criteria criteria = Criteria.where("roleName").is(roleName);
        query.addCriteria(criteria);
        Role roleInDB = mongoTemplate.findOne(query, Role.class);
        if (roleInDB == null)
            throw new NotExistException("不存在名为" + roleName + "的角色!");
        return roleInDB;
    }

    public Role findRoleById(String id) throws NotExistException {
        Query query = new Query();
        Criteria criteria = Criteria.where("_id").is(id);
        query.addCriteria(criteria);
        Role role = mongoTemplate.findOne(query, Role.class);
        if(role==null) {
            throw new NotExistException("角色不存在");
        }
        return role;
    }

    public void addRole(Role role) throws ExistException {
        Role roleInDB = null;
        try {
            roleInDB = findRoleByName(role.getRoleName());
        } catch (NotExistException e) {
        }
        if (roleInDB != null) {
            throw new ExistException("已存在名为" + role.getRoleName() + "的角色!");
        }
        mongoTemplate.insert(role);
    }

    public void deleteRole(String name) throws NotExistException {
        Role roleInDB = null;
        try {
            roleInDB = findRoleByName(name);
        } catch (NotExistException e) {
            throw new NotExistException(e.getMessage());
        }
        mongoTemplate.remove(roleInDB);
    }

    public void updateRole(Role role) {
        Update update = new Update();
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(role.getId()));
        update.set("permissions", role.getPermissions());
        mongoTemplate.updateFirst(query, update, Role.class);
    }

    public PageUtil<Role> findPage(int pageNo, int pageSize, String sortName, String sortOrder, String field, String value, int queryMethod) {
        Query query = new Query();
        if (field != null && !field.equals("") && value != null && !value.equals("")) {
            if (queryMethod == 0) {
                Pattern pattern = Pattern.compile("^.*" + value + ".*$", Pattern.CASE_INSENSITIVE);
                Criteria criteria = Criteria.where(field).regex(pattern);
                query.addCriteria(criteria);
            } else {
                Criteria criteria = Criteria.where(field).is(value);
                query.addCriteria(criteria);
            }
        }
        Long temp = mongoTemplate.count(query, Role.class);
        query.with(new Sort(Sort.Direction.fromString(sortOrder), sortName));
        query.skip((pageNo - 1) * pageSize);
        query.limit(pageSize);
        return new PageUtil<>(pageNo, pageSize, temp, mongoTemplate.find(query, Role.class));
    }

    public List<String> getAllRoles() {
        return mongoTemplate.getCollection("role").distinct("roleName");
    }

    //删除permission时，要把role中所有用到这个permission的记录都去掉这个权限
    public List<String> deleteRolePermission(Permission permission) {
        //保存修改过的role的id，后面需要更新user的role
        List<String> ids = new ArrayList<>();

        List<Role> roles = mongoTemplate.find(new Query(), Role.class);
        for (Role role: roles) {
            if(role.getPermissions().contains(permission)) {
                role.getPermissions().remove(permission);
                ids.add(role.getId());
                updateRole(role);
            }
        }

        return ids;
    }

}
