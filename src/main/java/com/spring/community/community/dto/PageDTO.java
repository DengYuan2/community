package com.spring.community.community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 封装分页信息
 */
@Data
public class PageDTO<T> {
    //分页形式参照Elastic中文社区
    private List<T> data;
    //<<  < 1 2 3 4 5 >  >>
    private boolean showFirstPage;
    private boolean showPrevious;
    private boolean showNext;
    private boolean showEndPage;
    private Integer page;
    private List<Integer> pages = new ArrayList<>();
    private Integer totalPage;

    public void setPageNation(Integer totalPage, Integer page) {
        this.totalPage=totalPage;
        this.page=page;
        System.out.println(totalPage);
        pages.clear();
        pages.add(page);//放入当前页码
        for (int i = 1; i <= 3; i++) {
            if (page - i > 0) {
                pages.add(0,page - i );
            }
            if (page + i <= totalPage) {
                pages.add(page + i);
            }
        }
        //点击第一页时
        if (page == 1) {
            showPrevious = false;
        } else {
            showPrevious = true;
        }
        //点击最后一页时
        if (page == totalPage) {
            showNext = false;
        } else {
            showNext = true;
        }
        //判断是否展示第1页
        if (pages.contains(1)) {
            showFirstPage = false;
        } else {
            showFirstPage = true;
        }
        //判断是否展示第1页
        if (pages.contains(totalPage)) {
            showEndPage = false;
        } else showEndPage = true;
    }
}
