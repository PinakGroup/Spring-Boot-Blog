package com.li.blog.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author li
 * @version 1.0
 * @since 18-10-23 下午9:04
 * 分页工具
 **/
public class PaginationUtil {
    public static List<Long> getPageRange(long currentPage, long totalPages) {
        long startPage = 1;
        long endPage = totalPages;
        //设置页码范围
        if (currentPage <= 2) {
            endPage = totalPages > 5 ? 5 : totalPages;
        } else if (currentPage >= totalPages - 2) {
            startPage = totalPages > 5 ? totalPages - 4 : 1;
        }
        List<Long> pageList = new ArrayList<>();
        for (long i = startPage; i <= endPage; i++) {
            pageList.add(i);
        }
        return pageList;
    }
}
