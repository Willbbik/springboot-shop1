package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.dto.QnADto;
import com.ecommerce.newshop1.entity.Item;
import com.ecommerce.newshop1.entity.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;

import java.util.List;

public interface QnAService  {


    List<QnADto> searchQnA(Item item, Pageable pageable);

    Long countQnAByItem(Item item);

    // QnA 관리자 답글 가져오기
    List<QnADto> getQnAReply(List<QnADto> qnaList);

    // 현재 사용자가 작성한 qnA가져오기
    List<QnADto> searchAllByMember(Long id, Member member);

    // QnA 질문 저장
    void saveQnA(QnADto dto, Long itemId) ;

    // QnA 관리자 답글 저장
    void saveQnAReply(QnADto dto, Long itemId);

    // view에 표시할 QnA답글 편집
    List<QnADto> editReply(List<QnADto> replyList);

    // view에 표시할 QnA질문 편집
    List<QnADto> editQna(List<QnADto> qnaList);

    Long getLastQnAId(List<QnADto> qnaList, Long lastQnAId);

    void deleteQnaAndReply(List<Long> qnaIdList);

}
