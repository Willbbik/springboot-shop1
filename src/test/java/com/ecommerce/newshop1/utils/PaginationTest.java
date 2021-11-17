package com.ecommerce.newshop1.utils;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PaginationTest {

    @Test
    public void ItemPaginationTest(){

        // given
        ItemPagination page = new ItemPagination(200L, 1);

        // then
        System.out.println(" curPage : " + page.getCurPage());
        System.out.println(" totalPage : " + page.getTotalPage());
        System.out.println(" startPage : " + page.getStartPage());
        System.out.println(" endPage : " + page.getEndPage());
        System.out.println(" nextPage : " + page.getNextPage());
        System.out.println(" prevPage : " + page.getPrevPage());

    }

    @Test
    public void qnaPaginationTest(){

        // given
        // when
        QnAPagination page = new QnAPagination(350L, 3);

        // then
        System.out.println(" curPage : " + page.getCurPage());
        System.out.println(" totalPage : " + page.getTotalPage());
        System.out.println(" startPage : " + page.getStartPage());
        System.out.println(" endPage : " + page.getEndPage());
        System.out.println(" nextPage : " + page.getNextPage());
        System.out.println(" prevPage : " + page.getPrevPage());
    }

}
