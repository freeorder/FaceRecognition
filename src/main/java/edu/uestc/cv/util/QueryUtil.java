package edu.uestc.cv.util;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.regex.Pattern;

public class QueryUtil {

    public static Query getPageQueryBothFuzzyAndExactMatch (String field, String value, int queryMethod) {
        Query query = new Query();
        boolean queryFlag = (field != null && !"".equalsIgnoreCase(field) && value != null && !"".equalsIgnoreCase(value));
        if (queryFlag) {
            if (queryMethod == 0) {
                if("male".equalsIgnoreCase(field)){
                    String content1 = "true";
                    String content2 = "false";
                    String pattern = ".*" + value + ".*";
                    boolean isMatch1 = Pattern.matches(pattern, content1);
                    boolean isMatch2 = Pattern.matches(pattern, content2);

                    if(isMatch1){
                        Criteria criteria;
                        criteria = Criteria.where("male").is(true);
                        query.addCriteria(criteria);
                    }else if(isMatch2){
                        Criteria criteria;
                        criteria = Criteria.where("male").is(false);
                        query.addCriteria(criteria);
                    }else {
                        Criteria criteria = Criteria.where(field).is(value);
                        query.addCriteria(criteria);
                    }
                }else if ("role".equalsIgnoreCase(field)){
                    Pattern pattern = Pattern.compile("^.*" + value + ".*$", Pattern.CASE_INSENSITIVE);
                    Criteria criteria;
                    criteria = Criteria.where("role.roleName").regex(pattern);
                    query.addCriteria(criteria);
                }else {
                    Pattern pattern = Pattern.compile("^.*" + value + ".*$", Pattern.CASE_INSENSITIVE);
                    Criteria criteria;
                    criteria = Criteria.where(field).regex(pattern);
                    query.addCriteria(criteria);
                }
            }
            else {
                if("male".equalsIgnoreCase(field)){
                    if("true".equalsIgnoreCase(value)){
                        Criteria criteria;
                        criteria = Criteria.where("male").is(true);
                        query.addCriteria(criteria);
                    }else if("false".equalsIgnoreCase(value)){
                        Criteria criteria;
                        criteria = Criteria.where("male").is(false);
                        query.addCriteria(criteria);
                    }else {
                        Criteria criteria = Criteria.where(field).is(value);
                        query.addCriteria(criteria);
                    }
                }else if ("role".equalsIgnoreCase(field)){
                    Criteria criteria;
                    criteria = Criteria.where("role.roleName").is(value);
                    query.addCriteria(criteria);
                }else{
                    Criteria criteria = Criteria.where(field).is(value);
                    query.addCriteria(criteria);
                }
            }
        }
        return query;
    }

    //给ProblemSet用的
    public static Criteria getPageCriteriaBothFuzzyAndExactMatch(String field, String value, int queryMethod) {
        Criteria criteria = null;
        boolean queryFlag = (field != null && !"".equalsIgnoreCase(field) && value != null && !"".equalsIgnoreCase(value));
        if (queryFlag) {
            if (queryMethod == 0) {
                Pattern pattern = Pattern.compile("^.*" + value + ".*$", Pattern.CASE_INSENSITIVE);
                criteria = Criteria.where(field).regex(pattern);
            } else {
                criteria = Criteria.where(field).is(value);
            }
        }
        return criteria;
    }
}
