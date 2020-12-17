package edu.uestc.cv.util;

import edu.uestc.cv.entity.Role;
import edu.uestc.cv.entity.User;

public class WhichRole {
    public static boolean isSystemAdmin(User user) {
        boolean is_admin_system = false;
        for (Role r : user.getRole()) {
            if ("admin".equals(r.getRoleName())) {
                is_admin_system = true;
                break;
            }
        }
        return is_admin_system;
    }

    public static boolean isSchoolAdmin(User user) {
        boolean is_admin_school = false;
        for (Role r : user.getRole()) {
            if ("schoolAdmin".equals(r.getRoleName())) {
                is_admin_school = true;
                break;
            }
        }
        return is_admin_school;
    }

    public static boolean isStudent(User user) {
        boolean is_student = false;
        for (Role r : user.getRole()) {
            if ("student".equals(r.getRoleName())) {
                is_student = true;
                break;
            }
        }
        return is_student;
    }

    public static boolean isTeacher(User user) {
        boolean is_teacher = false;
        for (Role r : user.getRole()) {
            if ("teacher".equals(r.getRoleName())) {
                is_teacher = true;
                break;
            }
        }
        return is_teacher;
    }
}
