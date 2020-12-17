package edu.uestc.cv.dao;

import edu.uestc.cv.entity.Permission;
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

import java.util.List;
import java.util.regex.Pattern;

@Repository
public class PermissionDAO {
    @Autowired
    MongoTemplate mongoTemplate;


    public Permission findPermissionByName(String permissionName) throws NotExistException {
        Query query = new Query();
        Criteria criteria = Criteria.where("permissionName").is(permissionName);
        query.addCriteria(criteria);
        Permission permissionInDB = mongoTemplate.findOne(query, Permission.class);
        if (permissionInDB == null)
            throw new NotExistException("不存在名为" + permissionName + "的权限!");
        return permissionInDB;

    }

    public void addPermission(Permission permission) throws ExistException {
        Permission permissionInDB = null;
        try {
            permissionInDB = findPermissionByName(permission.getPermissionName());
        } catch (NotExistException e) {
        }
        if (permissionInDB != null) {
            throw new ExistException("已存在名为" + permission.getPermissionName() + "的权限!");
        }
        mongoTemplate.insert(permission);
    }


    public void deletePermission(Permission permission) throws NotExistException {
        Permission permissionInDB = null;
        try {
            permissionInDB = findPermissionByName(permission.getPermissionName());
        } catch (NotExistException e) {
            throw new NotExistException("不存在名为" + permission.getPermissionName() + "的权限!");
        }
        mongoTemplate.remove(permissionInDB);
    }

    public void updatePermission(Permission permission) {
        Update update = new Update();
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(permission.getId()));
        update.set("permissionName", permission.getPermissionName());
        mongoTemplate.updateFirst(query, update, Permission.class);
    }

    public PageUtil<Permission> findPage(int pageNo, int pageSize, String sortName, String sortOrder, String field, String value, int queryMethod) {
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
        Long temp = mongoTemplate.count(query, Permission.class);
        query.with(new Sort(Sort.Direction.fromString(sortOrder), sortName));
        query.skip((pageNo - 1) * pageSize);
        query.limit(pageSize);
        return new PageUtil<>(pageNo, pageSize, temp, mongoTemplate.find(query, Permission.class));
    }


    public List<String> getAllPermissions() {
        return mongoTemplate.getCollection("permission").distinct("permissionName");
    }

//    public void deleteUserPermission(String permission) {
//        Query query = new Query();
//        Criteria criteria = Criteria.where("permissions");
//        query.addCriteria(criteria);
//    }
}
