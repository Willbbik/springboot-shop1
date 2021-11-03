package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.dto.QnADto;
import com.ecommerce.newshop1.entity.ProductEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;

import java.util.List;

public interface QnAService  {

    // QnA들 가져오기
    String getQnAList(Long productId, Model model, int page) throws Exception;

    // QnA 질문 개수 가져오기
    int getQnaSize(Long productId);

    // QnA 관리자 답글 가져오기
    List<QnADto> getQnAReply(List<QnADto> qnaList);

    // QnA 관리자 답글 저장
    void saveQnAAnswer(QnADto dto);

    // QnA 질문 저장
    void saveQnAQuestion(QnADto dto) throws Exception;

    // view에 표시할 QnA답글 편집
    List<QnADto> editReply(List<QnADto> replyList);

    // view에 표시할 QnA질문 편집
    List<QnADto> editQna(ProductEntity productId, Pageable pageable) throws Exception;

    // QnA 페이징을 위한 pageable 편집
    Pageable editQnAPageable(int page, int qnaSize);

    // QnA 질문 저장하기 전에 유효성 검사
    int checkValidationQnA(QnADto dto);


}
