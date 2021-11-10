package com.ecommerce.newshop1.utils;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PaginationTest {



    @Test
    public void calculateTest(){

        // given
        ItemPagination page = new ItemPagination();
        page.setCurPage(4);
        page.setTotalPost(0L);

        // when
        page.calculate();

        // then
        System.out.println(" curPage : " + page.getCurPage());
        System.out.println(" totalPage : " + page.getTotalPage());
        System.out.println(" startPage : " + page.getStartPage());
        System.out.println(" endPage : " + page.getEndPage());
        System.out.println(" nextPage : " + page.getNextPage());
        System.out.println(" prevPage : " + page.getPrevPage());

    }

}
