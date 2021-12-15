package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.dto.QnADto;
import com.ecommerce.newshop1.entity.Item;
import com.ecommerce.newshop1.entity.Member;
import com.ecommerce.newshop1.entity.QnAEntity;
import com.ecommerce.newshop1.repository.ItemRepository;
import com.ecommerce.newshop1.repository.QnARepository;
import com.ecommerce.newshop1.utils.QnAPagination;
import com.ecommerce.newshop1.utils.enums.QnA;
import com.ecommerce.newshop1.utils.enums.Role;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class QnAServiceImpl implements QnAService{

    private final ItemRepository itemRepository;
    private final QnARepository qnARepository;
    private final SecurityService security;
    private final MemberService memberService;

    ModelMapper mapper = new ModelMapper();

    @Override
    @Transactional
    public String getQnAHtml(Long itemId, Model model, int curPage) throws Exception {

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다 상품 번호 : " + itemId));

        // 해당 상품의 qna 개수
        Long qnaSize = qnARepository.countByItem(item);

        // 페이징 처리
        QnAPagination page = new QnAPagination(qnaSize, curPage);
        Pageable pageable = PageRequest.of(page.getCurPage() - 1, page.getShowMaxQnA());

        // qna 존재유무 확인해서 값 담기
        boolean qnaExists = qnARepository.existsByItem(item);

        List<QnADto> qnaList = (!qnaExists) ? new ArrayList<>() : qnARepository.searchQnA(item, pageable);
        List<QnADto> qnaReplyList = getQnAReply(qnaList);

        // 값 편집 ( 비공개 글 일 때는 내용을 숨겨야 하기 때문에 )
        qnaList = editQna(qnaList);               // QnA 편집
        qnaReplyList = editReply(qnaReplyList);   // QnA 답글 편집

        model.addAttribute("page", page);
        model.addAttribute("qnaSize", qnaSize);
        model.addAttribute("qnaList", qnaList);
        model.addAttribute("qnaReplyList", qnaReplyList);

        return "item/tab/tab3QnA";
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

        if(qnaList != null) {
            List<QnADto> replyList = new ArrayList<>();

            for (QnADto parent : qnaList) {
                Optional<QnAEntity> reply = qnARepository.findByParent(parent.getId());

                // 존재유무 확인
                if (reply.isPresent()) replyList.add(mapper.map(reply.get(), QnADto.class));
                else replyList.add(null);
            }
            return replyList;
        }
        return null;
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

        // 관리자 답글의 writer, content 수정
        for(QnADto reply : replyList){

            if(reply == null){
                replyResult.add(reply);
                continue;
            }

            // qna 작성자 아이디값 가져오기
            QnAEntity qnaWriter = qnARepository.findById(reply.getParent())
                    .orElseThrow(() -> new IllegalArgumentException("해당 QnA가 존재하지 않습니다. QnA Id = " + reply.getParent()));

            reply.setWriter("판매자");

            if(reply.getHide().equals(QnA.PRIVATE.getValue())){

                // 현재 로그인한 사람이 qna 작성자인지 아니 관리자인지 확인하기 위해서
                if(!security.compareName(qnaWriter.getWriter()) && !security.checkHasRole(Role.ADMIN.getValue())){
                    reply.setContent("비밀글입니다.");
                }
            }
            replyResult.add(reply);
        }
        return replyResult;
    }

    @Override
    @Transactional(readOnly = true)
    public List<QnADto> editQna(List<QnADto> qnaList){

        if(qnaList.isEmpty()){
            return null;
        }
        // 값들 수정해주기
        for (QnADto dto : qnaList) {
            // 아이디 마스킹
            dto.setWriter(dto.getWriter().substring(0, 3) + "***");

            if(dto.getContent().length() > 30){
                dto.setTitle(dto.getContent().substring(0, 30) + "...");
            } else { dto.setTitle(dto.getContent()); }

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
        }
        return qnaList;
    }



    @Override
    @Transactional
    public void saveQnAReply(QnADto dto){
        Member member = memberService.getCurrentMember();

        QnAEntity qnaReply = QnAEntity.builder()
                .member(member)
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

        Item item = dto.getItem();
        item.setQnaEntityList(qnaReply);
        itemRepository.save(item);
    }


    @Override
    @Transactional
    public void saveQnA(QnADto dto) {
        Member member = memberService.getCurrentMember();

        QnAEntity qnaEntity = QnAEntity.builder()
                .member(member)
                .writer(member.getUserId())
                .content(dto.getContent().replaceAll("\\s+", " "))
                .hide(dto.getHide())
                .parent(null)
                .depth(1)
                .replyEmpty(QnA.EMPTY.getValue())
                .build();

        Item item = dto.getItem();
        item.setQnaEntityList(qnaEntity);
        itemRepository.save(item);
    }


    @Override
    public int checkValidationQnA(QnADto dto){
        // 정상 = 0
        // 공백 or 길이초과 or 값 조작 있을시 = -1
        // 비로그인 = -2

        String content = dto.getContent();

        if(content.isEmpty() || content.isBlank() || content.length() > 2048){
            return -1;
        }
        if(!security.isAuthenticated()){
            return -2;
        }

        if(dto.getHide().equals(QnA.PRIVATE.getValue())) return 0;
        else if(dto.getHide().equals(QnA.PUBLIC.getValue())) return 0;
        else return -1;
    }


}
