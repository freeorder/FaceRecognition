package edu.uestc.cv.constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SortConstant {
    public static final String DEFAULT_SORT_NAME = "id";

    public static final String DEFAULT_SORT_ORDER = "DESC";

    public static final List<String> SORT_ORDERS = new ArrayList<>(Arrays.asList("ASC", "DESC"));

    public static boolean isValidateSortOrder(String sortOrder) {
        return SORT_ORDERS.contains(sortOrder.toUpperCase());
    }
}
