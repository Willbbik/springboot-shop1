package com.ecommerce.newshop1.utils;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QnAPagination {

    private int showMaxQnA = 3;   // 한 페이지에 보여질 수 있는 QnA의 최대 개수
    private int showPageNum = 10; // 총 표시될 페이지 버튼 개수
    private Long totalQnA;
    private int totalPage;
    private int curPage;
    private int startPage;
    private int endPage;
    private int prevPage;
    private int nextPage;


    public QnAPagination(Long totalQnA, int curPage){

        this.totalQnA = totalQnA;
        this.curPage = curPage;

        // 페이지 버튼 총 개수
        this.totalPage = (this.totalQnA % showMaxQnA == 0) ?
                (int) (this.totalQnA / showMaxQnA) :
                (int) (this.totalQnA / showMaxQnA + 1);
        this.totalPage = Math.max(this.totalPage, 1);

        // 현재 페이지 번호
        this.curPage = Math.min(this.curPage, this.totalPage);
        this.curPage = Math.max(this.curPage, 1);

        // 현재 페이지의 시작 버튼
        this.startPage = (this.curPage % 10 == 0) ?
                ((this.curPage - 1) / 10) * 10 + 1 :
                (this.curPage / 10) * 10 + 1;

        // 현재 페이지의 마지막 버튼
        this.endPage = (this.curPage % 10 == 0) ?
                this.curPage : ((this.curPage / 10) + 1) * 10;
        this.endPage = Math.min(endPage, totalPage);

        // 다음 페이지 버튼
        this.nextPage = (this.curPage % 10 == 0) ?
                this.curPage + 1 : (((this.curPage / 10) + 1) * 10 + 1);
        this.nextPage = Math.min(nextPage, totalPage);

        // 이전 페이지 버튼
        this.prevPage = (((this.curPage - 11) / 10) * 10 + 1);
        this.prevPage = Math.max(prevPage, 1);

    }


}
