package com.ecommerce.newshop1.utils;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Pagination {


    private int showMaxPage = 3;    // 한 페이지에 보여질 수 있는 QnA의 최대 개수
    private int blockSize = 10;     // 한 페이지에 보여질 수 있는 페이지 버튼 블록 최대 개수
    private int curPage = 1;        // default 현재 페이지
    private Long totalPost;
    private int totalPage;
    private int lastPage;
    private int nextBlock;
    private int prevBlock;



    public Pagination(Long qnaTotal, int curPage){

        // 현재 페이지 번호
        setCurPage(curPage);

        // QnA 총 개수
        setTotalPost(qnaTotal);

        // 총 페이지 블럭 개수
        setTotalPage((int) Math.round((qnaTotal * 1.0) / showMaxPage));

        // 마지막 페이지 번호
        setLastPage((int) Math.round((qnaTotal * 1.0) / showMaxPage));

        // 다음 버튼
        setNextBlock(((curPage / 10) + 1) * 10 + 1);
        if(getNextBlock() > getTotalPage()) {
            setNextBlock(getTotalPage());
        }

        // 이전 버튼
        setPrevBlock(((curPage - 11) / 10) * 10 + 1);


    }


}
