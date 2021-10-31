//package com.ecommerce.newshop1.utils;
//
//
//import lombok.Builder;
//import lombok.Getter;
//import lombok.Setter;
//
//@Getter
//@Setter
//@Builder
//public class Pagination {
//
//    // 한 페이지에 보여질 수 있는 QnA의 최대 개수
//    private int showMaxPage = 3;
//
//    private int blockSize = 10;
//    private int curPage = 1;
//    private int block;
//    private Long totalPost;
//    private int totalPage;
//    private int startPage = 1;
//    private int lastPage = 1;
//    private int startIndex = 0;
//    private int nextBlock;
//    private int prevBlock;
//
//
//
//    public Pagination(Long qnaTotalSize, int curPage){
//
//        // 현재 페이지
//        setCurPage(curPage);
//
//        // QnA 총 개수
//        setTotalPost(qnaTotalSize);
//
//        // 총 페이지 수
//        setTotalPage((int) Math.ceil(qnaTotalSize * 1.0 / showMaxPage));
//
//        // 총 블럭 수
//        setTotal
//
//
//    }
//
//
//}
