package com.ecommerce.newshop1.utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaginationShowSizeTen {

    private int showMaxSize = 10;   // 표시되는 게시물의 최대 개수
    private int showPageNum = 10;   // 표시되는 페이지 버튼의 최대 개수
    private Long totalSize;
    private int totalPage;
    private int curPage;
    private int startPage;
    private int endPage;
    private int prevPage;
    private int nextPage;


    public PaginationShowSizeTen(Long total, int curPage){
        // 현재 페이지 번호에서 -1 해서 curPage에 적용

        this.totalSize = total;
        this.curPage = curPage;

        // 페이지 버튼 총 개수
        this.totalPage = (this.totalSize % showMaxSize == 0) ?
                (int) (this.totalSize / showMaxSize) :
                (int) (this.totalSize / showMaxSize + 1);
        this.totalPage = Math.max(this.totalPage, 1);

        // 현재 페이지 번호
        this.curPage = Math.min(this.curPage, this.totalPage);
        this.curPage = Math.max(this.curPage, 1);

        // 현재 페이지의 시작 버튼 번호
        this.startPage = (this.curPage % showMaxSize == 0) ?
                ((this.curPage - 1) / showMaxSize) * showMaxSize + 1 :
                (this.curPage / showMaxSize) * showMaxSize + 1;

        // 현재 페이지의 마지막 버튼 번호
        this.endPage = (this.curPage % showMaxSize == 0) ?
                this.curPage : ((this.curPage / showMaxSize) + 1) * showPageNum;
        this.endPage = Math.min(this.endPage, totalPage);


        // 다음 페이지 버튼 ( 10칸 앞으로 이동 )
        this.nextPage = (this.curPage % showMaxSize == 0) ?
                this.curPage + 1 : (((this.curPage / showMaxSize) + 1) * showPageNum + 1);
        this.nextPage = Math.min(this.nextPage, totalPage);


        // 이전 페이지 버튼 ( 10칸 뒤로 이동 )
        this.prevPage = (((this.curPage - 11) / showMaxSize) * showPageNum + 1);
        this.prevPage = Math.max(this.prevPage, 1);

    }

}
