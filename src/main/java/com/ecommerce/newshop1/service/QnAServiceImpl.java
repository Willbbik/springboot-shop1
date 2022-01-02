package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.dto.QnADto;
import com.ecommerce.newshop1.entity.Item;
import com.ecommerce.newshop1.entity.Member;
import com.ecommerce.newshop1.entity.QnAEntity;
import com.ecommerce.newshop1.repository.QnARepository;
import com.ecommerce.newshop1.enums.QnA;
import com.ecommerce.newshop1.enums.Role;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class QnAServiceImpl implements QnAService{

    private final QnARepository qnARepository;
    private final SecurityService security;
    private final MemberService memberService;
    private final ItemService itemService;

    ModelMapper mapper = new ModelMapper();


    @Override
    @Transactional(readOnly = true)
    public Long countQnAByItem(Item item) {
        return qnARepository.countQnAByItem(item);
    }

    @Override
    @Transactional
    public List<QnADto> searchQnA(Item item, Pageable pageable) {
        return qnARepository.searchQnA(item, pageable);
    }

    @Override
    public Long getLastQnAId(List<QnADto> qnaList, Long lastQnAId) {
    // nooffset 페이징을 위해서 마지막 qna번호 가져오기

        if(qnaList != null) {  // 댓글이 더 존재하다면 계산후 리턴

            if (qnaList.size() > 1) {   //
                lastQnAId = qnaList.get(qnaList.size() - 1).getId();
            } else if (qnaList.size() == 1) {
                lastQnAId = qnaList.get(0).getId();
            }
            return lastQnAId;
        }                       // 존재하지 않을때는 파라미터로 온 id값 리턴
        return lastQnAId;
    }


    @Override
    @Transactional(readOnly = true)
    public List<QnADto> getQnAReply(List<QnADto> qnaList) {

        if(!qnaList.isEmpty()) {
            List<QnADto> replyList = new ArrayList<>();

            for (QnADto parent : qnaList) {
                Optional<QnAEntity> reply = qnARepository.findByParent(parent.getId());

                // 존재유무 확인
                if (reply.isPresent()) replyList.add(mapper.map(reply.get(), QnADto.class));
                else replyList.add(null);
            }
            return replyList;
        }
        return new ArrayList<>();
    }

    @Override
    @Transactional(readOnly = true)
    public List<QnADto> searchAllByMember(Long id, Member member){

        return qnARepository.searchAllByMember(id, member);
    }

    @Override
    @Transactional(readOnly = true)
    public List<QnADto> editReply(List<QnADto> replyList){

        List<QnADto> replyResult = new ArrayList<>();

        if(!replyList.isEmpty()) {

            // 관리자 답글의 writer, content 수정
            for (QnADto reply : replyList) {

                if (reply == null) {
                    replyResult.add(reply);
                    continue;
                }

                // qna 작성자 아이디값 가져오기
                QnAEntity qnaWriter = qnARepository.findById(reply.getParent())
                        .orElseThrow(() -> new IllegalArgumentException("해당 QnA가 존재하지 않습니다. QnA Id = " + reply.getParent()));

                reply.setWriter("판매자");

                if (reply.getHide().equals(QnA.PRIVATE.getValue())) {

                    // 현재 로그인한 사람이 qna 작성자인지 아니 관리자인지 확인하기 위해서
                    if (!security.compareName(qnaWriter.getWriter()) && !security.checkHasRole(Role.ADMIN.getValue())) {
                        reply.setContent("비밀글입니다.");
                    }
                }
                replyResult.add(reply);
            }
            return replyResult;
        }
        return new ArrayList<>();
    }

    @Override
    @Transactional(readOnly = true)
    public List<QnADto> editQna(List<QnADto> qnaList){

        if(!qnaList.isEmpty()) {

            // 값들 수정해주기
            for (QnADto dto : qnaList) {

                if (dto.getContent().length() > 30) {
                    dto.setTitle(dto.getContent().substring(0, 30) + "...");
                } else {
                    dto.setTitle(dto.getContent());
                }

                if (dto.getHide().equals(QnA.PRIVATE.getValue())) {
                    if (!security.compareName(dto.getWriter()) && !security.checkHasRole(Role.ADMIN.getValue())) {
                        dto.setContent("비밀글입니다.");
                        dto.setTitle("비밀글입니다.");
                    }
                }

                // 답글 유무 확인
                boolean result = qnARepository.existsByParent(dto.getId());

                if (result) {
                    dto.setReplyEmpty("답변완료");
                } else {
                    dto.setReplyEmpty("답변대기");
                }
                dto.setWriter(dto.getWriter().substring(0, 3) + "***");

            }
            return qnaList;
        }

        return new ArrayList<>();
    }

    @Override
    @Transactional
    public void saveQnA(QnADto dto, Long itemId) {

        Member member = memberService.getCurrentMember();
        Item item = itemService.findById(itemId);

        QnAEntity qnaEntity = QnAEntity.builder()
                .writer(member.getUserId())
                .content(dto.getContent().replaceAll("\\s+", " "))
                .hide(dto.getHide())
                .parent(null)
                .depth(1)
                .replyEmpty(QnA.EMPTY.getValue())
                .build();

        item.addQnaList(qnaEntity);
        member.addQnaList(qnaEntity);

        qnARepository.save(qnaEntity);
    }

    @Override
    @Transactional
    public void saveQnAReply(QnADto dto, Long itemId){

        Member member = memberService.getCurrentMember();
        Item item = itemService.findById(itemId);

        QnAEntity qnaReply = QnAEntity.builder()
                .writer(member.getUserId())
                .content(dto.getContent())
                .parent(dto.getParent())
                .hide(dto.getHide())
                .replyEmpty(QnA.EMPTY.getValue())
                .depth(2)
                .build();

        QnAEntity parentQna = qnARepository.findById(dto.getParent()).get();
        // 해당 질문 답변 상태 변경. 답변없음 > 답변존재
        parentQna.setReplyEmpty(QnA.PRESENT.getValue());

        item.addQnaList(qnaReply);
        member.addQnaList(qnaReply);

        qnARepository.save(qnaReply);
    }



    @Override
    @Transactional
    public void deleteQnaAndReply(List<Long> qnaIdList) {

        qnARepository.deleteAllById(qnaIdList);
        qnARepository.deleteAllByParentIn(qnaIdList);
    }


}
