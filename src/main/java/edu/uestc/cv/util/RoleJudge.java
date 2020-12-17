package edu.uestc.cv.util;

import edu.uestc.cv.constant.RoleConstant;
import edu.uestc.cv.entity.Role;
import edu.uestc.cv.entity.User;

public class RoleJudge {
    public static boolean isSchoolAdmin(User user) {
        return user.getRole().contains(new Role(RoleConstant.ADMIN_COMPANY));
    }

    public static boolean isSystemAdmin(User user) {
        return user.getRole().contains(new Role(RoleConstant.ADMIN_SYSTEM));
    }

    public static boolean isAdmin(User user) {
        return user.getRole().contains(new Role(RoleConstant.ADMIN_SYSTEM)) ||
                user.getRole().contains(new Role(RoleConstant.ADMIN_COMPANY));
    }

    public static boolean isTeacher(User user) {
        return user.getRole().contains(new Role(RoleConstant.TEACHER));
    }

    public static boolean isStudent(User user) {
        return user.getRole().contains(new Role(RoleConstant.STAFF));
    }
}
