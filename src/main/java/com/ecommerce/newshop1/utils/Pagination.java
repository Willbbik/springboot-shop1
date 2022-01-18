package com.ecommerce.newshop1.utils;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Pagination {

    private int showMaxSize;   // 한 페이지에 보여질 수 있는 게시물의 최대 개수
    private int showPageBtnSize;  // 보여질 수 있는 페이지 버튼 최대 개수
    private Long totalPost;
    private int totalPage;
    private int curPage;
    private int startPage;
    private int endPage;
    private int prevPage;
    private int nextPage;


    public Pagination(Long totalPost, int curPage, int showMaxSize, int showPageBtnSize){

        this.showMaxSize = showMaxSize;
        this.showPageBtnSize = showPageBtnSize;
        this.totalPost = totalPost;
        this.curPage = curPage;

        // 페이지 버튼 총 개수
        this.totalPage = (this.totalPost % showMaxSize == 0) ?
                (int) (this.totalPost / showMaxSize) :
                (int) (this.totalPost / showMaxSize + 1);
        this.totalPage = Math.max(this.totalPage, 1);

        // 현재 페이지 번호
        this.curPage = Math.min(this.curPage, this.totalPage);
        this.curPage = Math.max(this.curPage, 1);

        // 현재 페이지의 시작 버튼
        this.startPage = (this.curPage % showPageBtnSize == 0) ?
                ((this.curPage - 1) / showPageBtnSize) * showPageBtnSize + 1 :
                (this.curPage / showPageBtnSize) * showPageBtnSize + 1;

        // 현재 페이지의 마지막 버튼
        this.endPage = (this.curPage % showPageBtnSize == 0) ?
                this.curPage : ((this.curPage / showPageBtnSize) + 1) * showPageBtnSize;
        this.endPage = Math.min(endPage, totalPage);

        // 다음 페이지 버튼
        this.nextPage = (this.curPage % showPageBtnSize == 0) ?
                this.curPage + 1 : (((this.curPage / showPageBtnSize) + 1) * showPageBtnSize + 1);
        this.nextPage = Math.min(nextPage, totalPage);

        // 이전 페이지 버튼
        this.prevPage = (((this.curPage - 11) / showPageBtnSize) * showPageBtnSize + 1);
        this.prevPage = Math.max(prevPage, 1);

    }


}
