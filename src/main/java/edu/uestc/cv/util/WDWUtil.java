package edu.uestc.cv.util;

public class WDWUtil {

    // @描述：是否是2003的excel，返回true是2003
    public static boolean isExcel2003(String filePath)  {
//        return filePath.matches("^.+\\.(xls)$");
        String fileType = filePath.substring(filePath.lastIndexOf(".") + 1);
        return "xls".equals(fileType);
    }

    //@描述：是否是2007的excel，返回true是2007
    public static boolean isExcel2007(String filePath)  {
//        return filePath.matches("^.+\\.(xlsx)$");
        String fileType = filePath.substring(filePath.lastIndexOf(".") + 1);
        return "xlsx".equals(fileType);
    }

    public static boolean isExcel (String filePath) {
        String fileType = filePath.substring(filePath.lastIndexOf(".") + 1);
        if ("xls".equals(fileType)) {
            return true;
        } else return "xlsx".equals(fileType);
    }

}
