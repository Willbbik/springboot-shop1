package com.ecommerce.newshop1.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemPagination {

    // 총 게시글과 현재 페이지만 받아옴

    private int showPageNum = 10;
    private Long totalPost;
    private int totalPage;
    private int curPage;
    private int startPage;
    private int endPage;
    private int nextPage;
    private int prevPage;


    public void calculate(){

        // 시작 페이지 번호

        // 페이지 버튼 총 개수
        this.totalPage = (this.totalPost % 10 == 0) ?
                (int) (this.totalPost / 10) :
                (int)Math.ceil(this.totalPost / 10 + 1);

        // 현재 페이지 번호
        this.curPage = (curPage <= 0) ? 1 : curPage;
        this.curPage = (this.curPage > this.totalPage) ? 1 : this.curPage;

        // 현재 페이지의 시작 버튼 번호
        this.startPage = (this.curPage % 10 == 0) ?
                ((this.curPage - 1) / 10) * 10 + 1 :
                (this.curPage / 10) * 10 + 1;

        // 현재 페이지의 마지막 버튼 번호
        this.endPage = (int)Math.ceil((this.curPage / 10) + 1) * 10;
        this.endPage = Math.min(this.endPage, totalPage);

        // 다음 페이지 버튼 ( 10칸 앞으로 이동 )
        this.nextPage = (this.curPage % 10 == 0) ?
                this.curPage + 1 : (((this.curPage / 10) + 1) * 10 + 1);
        this.nextPage = Math.min(this.nextPage, totalPage);

        // 이전 페이지 버튼 ( 10칸 뒤로 이동 )
        this.prevPage = (((this.curPage - 11) / 10) * 10 + 1);
        this.prevPage = Math.max(this.prevPage, 1);

    }


}
