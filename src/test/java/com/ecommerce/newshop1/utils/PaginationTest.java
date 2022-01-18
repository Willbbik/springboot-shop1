package com.ecommerce.newshop1.utils;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PaginationTest {

    @Test
    void Pagination(){

        //givien
        Pagination page = new Pagination(230L, 2, 9, 10);

        // then
        System.out.println(" curPage : " + page.getCurPage());
        System.out.println(" totalPage : " + page.getTotalPage());
        System.out.println(" startPage : " + page.getStartPage());
        System.out.println(" endPage : " + page.getEndPage());
        System.out.println(" nextPage : " + page.getNextPage());
        System.out.println(" prevPage : " + page.getPrevPage());

    }

    @Test
    public void PaginationSizeTenTest(){

        //given
        PaginationShowSizeTen page = new PaginationShowSizeTen(200L, 1);

        // then
        System.out.println(" curPage : " + page.getCurPage());
        System.out.println(" totalPage : " + page.getTotalPage());
        System.out.println(" startPage : " + page.getStartPage());
        System.out.println(" endPage : " + page.getEndPage());
        System.out.println(" nextPage : " + page.getNextPage());
        System.out.println(" prevPage : " + page.getPrevPage());
    }

    @Test
    public void PaginationSizeThreeTest(){

        // given
        PaginationShowSizeThree page = new PaginationShowSizeThree(350L, 3);

        // then
        System.out.println(" curPage : " + page.getCurPage());
        System.out.println(" totalPage : " + page.getTotalPage());
        System.out.println(" startPage : " + page.getStartPage());
        System.out.println(" endPage : " + page.getEndPage());
        System.out.println(" nextPage : " + page.getNextPage());
        System.out.println(" prevPage : " + page.getPrevPage());
    }

}
