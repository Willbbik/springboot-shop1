package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.dto.ItemQnADto;
import com.ecommerce.newshop1.dto.ItemQnAReplyDto;
import com.ecommerce.newshop1.entity.Item;
import com.ecommerce.newshop1.entity.ItemQnA;
import com.ecommerce.newshop1.entity.ItemQnAReply;
import com.ecommerce.newshop1.entity.Member;
import com.ecommerce.newshop1.enums.Role;
import com.ecommerce.newshop1.repository.QnAReplyRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QnAReplyServiceImpl implements QnAReplyService{

    private final QnAReplyRepository qnaReplyRepository;
    private final QnAService qnaService;
    private final MemberService memberService;
    private final ItemService itemService;
    private final SecurityService security;

    ModelMapper mapper = new ModelMapper();


    @Override
    @Transactional
    public Long save(ItemQnAReplyDto replyDto, Long itemId, Long qnaId) {

        Member member = memberService.getCurrentMember();
        ItemQnA qna = qnaService.findById(qnaId);
        Item item = itemService.findById(itemId);

        ItemQnAReply qnaReply = mapper.map(replyDto, ItemQnAReply.class);
        qnaReply.setWriter(member.getUserId().substring(0, 3) + "***");

        qna.setQnAReply(qnaReply);
        member.addQnaReplyList(qnaReply);
        item.addQnaReplyList(qnaReply);

        return qnaReplyRepository.save(qnaReply).getId();
    }

    @Override
    @Transactional
    public List<ItemQnAReplyDto> findAllByQnA(List<ItemQnADto> qnaList) {

        List<ItemQnAReplyDto> replyList = new ArrayList<>();
        for(ItemQnADto dto : qnaList){
            replyList.add(qnaReplyRepository.findByQnA(dto.getId()));
        }

        return replyList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemQnAReplyDto> searchAllByQnA(List<ItemQnADto> qnaList) {

        List<ItemQnAReplyDto> replyList = new ArrayList<>();

        qnaList.stream()
                    .forEach(
                            p -> replyList.add(qnaReplyRepository.searchAllByQnA(p.getId()))
                    );

        return replyList;
    }


    @Override
    @Transactional(readOnly = true)
    public List<ItemQnAReplyDto> edit(List<ItemQnAReplyDto> replyList) {

        String hide = "private";
        if(!replyList.isEmpty()){
            if(!security.isAuthenticated()){
                replyList.stream()
                        .filter(p -> p.getHide().equals(hide))
                        .filter(p -> !security.checkHasRole(Role.ADMIN.getValue()))
                        .filter(p -> ! p.getQna().getMember().getUserId().equals(security.getName()))
                        .forEach(p -> p.setContent("비밀글입니다"));
            }else{
                replyList.stream()
                        .filter(p -> p.getHide().equals(hide))
                        .forEach(p -> p.setContent("비밀글입니다"));
            }
        }
        return replyList;
    }



}
