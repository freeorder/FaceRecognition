package edu.uestc.cv.util;

import java.util.List;


public class PageUtil<T> {
    public int pageNo;
    public int pageSize;
    public int totalPages;
    public long totalElements;
    public List<T> content;


    public PageUtil() {
        super();
    }

//    public PageUtil(int pageNo, int pageSize, int totalPage, long totalElements, List<T> content) {
//        this.pageNo = pageNo;
//        this.pageSize = pageSize;
//        this.totalPages = totalPage;
//        this.totalElements = totalElements;
//        this.content = content;
//    }


    public PageUtil(Integer pageNo, Integer pageSize, List<T> workList) {

        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.totalElements = workList.size();
        this.totalPages = (int) (totalElements % pageSize == 0 ? totalElements / pageSize : totalElements / pageSize + 1);

        long start = (pageNo - 1) * pageSize < totalElements ? (pageNo - 1) * pageSize : totalElements;
        long end = pageNo * pageSize < totalElements ? pageNo * pageSize : totalElements;

        this.content = workList.subList((int) start, (int) end);
    }

    public PageUtil(Integer pageNo, Integer pageSize, long totalElements, List<T> workList) {

        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.totalPages = (int) (totalElements % pageSize == 0 ? totalElements / pageSize : totalElements / pageSize + 1);
        this.content = workList;
    }


    @Override
    public String toString() {
        return "PageEntity{" + "pageNo=" + pageNo + ", pageSize=" + pageSize + ", totalPages=" + totalPages + ", totalElements="
                + totalElements + ", content=" + content + '}';
    }
}
