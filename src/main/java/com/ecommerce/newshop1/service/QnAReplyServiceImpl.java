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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        qna.setReplyEmpty(false);
        member.addQnaReplyList(qnaReply);
        item.addQnaReplyList(qnaReply);

        return qnaReplyRepository.save(qnaReply).getId();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemQnAReplyDto> findAllByQnA(List<ItemQnADto> qnaList) {

        List<ItemQnAReplyDto> replyList = new ArrayList<>();
        for(ItemQnADto dto : qnaList){
            Optional<ItemQnAReply> reply = qnaReplyRepository.findByItemQnAId(dto.getId());
            reply.ifPresent(qnAReply -> replyList.add(mapper.map(qnAReply, ItemQnAReplyDto.class)));
        }

        return replyList;
    }


    @Override
    @Transactional(readOnly = true)
    public List<ItemQnAReplyDto> edit(List<ItemQnAReplyDto> replyList) {

        if(!replyList.isEmpty()){
            if(security.isAuthenticated()){
                replyList.stream()
                        .filter(p -> p.getHide().equals("private"))
                        .filter(p -> !security.checkHasRole(Role.ADMIN.getValue()))
                        .filter(p -> ! p.getItemQnA().getMember().getUserId().equals(security.getName()))
                        .forEach(p -> p.setContent("비밀글입니다"));
            }else{
                replyList.stream()
                        .filter(p -> p.getHide().equals("private"))
                        .forEach(p -> p.setContent("비밀글입니다"));
            }
        }
        return replyList;
    }



}
