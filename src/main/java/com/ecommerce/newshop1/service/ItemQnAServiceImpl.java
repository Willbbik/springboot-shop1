package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.dto.ItemQnADto;
import com.ecommerce.newshop1.entity.Item;
import com.ecommerce.newshop1.entity.Member;
import com.ecommerce.newshop1.entity.ItemQnA;
import com.ecommerce.newshop1.enums.Role;
import com.ecommerce.newshop1.exception.QnANotFoundException;
import com.ecommerce.newshop1.repository.ItemQnARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ItemQnAServiceImpl implements ItemQnAService {

    private final ItemQnARepository qnaRepository;
    private final MemberService memberService;
    private final ItemService itemService;
    private final SecurityService security;

    @Override
    @Transactional(readOnly = true)
    public ItemQnA findById(Long qnaId) {
        return qnaRepository.findById(qnaId)
                .orElseThrow(() -> new QnANotFoundException("존재하지 않는 qna입니다."));
    }

    @Override
    @Transactional(readOnly = true)
    public Long countByItem(Item item) {
        return qnaRepository.countByItem(item);
    }


    @Override
    public Long getLastQnAId(List<ItemQnADto> qnaList, Long lastQnAId) {

        if(qnaList.isEmpty()){
            return lastQnAId;
        }
        return qnaList.stream()
                .min(Comparator.comparingLong(ItemQnADto::getId))
                .get().getId();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemQnADto> searchAllByItem(Item item, Pageable pageable) {
        return qnaRepository.searchAllByItem(item, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemQnADto> searchAllByMember(Long id, Member member, Pageable pageable){

        return qnaRepository.searchAllByMember(id, member, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemQnADto> edit(List<ItemQnADto> qnaList){

        if(!qnaList.isEmpty()){
            if(security.isAuthenticated()){
                qnaList.stream()
                        .filter(p -> p.getHide().equals("private"))
                        .filter(p -> !security.checkHasRole(Role.ADMIN.getValue()))
                        .filter(p -> !p.getMember().getUserId().equals(security.getName()))
                        .forEach(p -> {
                            p.setContent("비밀글입니다.");
                            p.setTitle("비밀글입니다.");
                        });
            }else{
                qnaList.stream()
                        .filter(p -> p.getHide().equals("private"))
                        .forEach(p -> {
                            p.setContent("비밀글입니다.");
                            p.setTitle("비밀글입니다.");
                        });
            }
        }
        return qnaList;
    }

    @Override
    @Transactional
    public Long save(ItemQnADto dto, Long itemId) {

        Member member = memberService.getCurrentMember();
        Item item = itemService.findById(itemId);

        if(dto.getContent().length() > 30) dto.setTitle(dto.getContent().substring(0, 30) + "...");
        else dto.setTitle(dto.getContent());

        ItemQnA qna = ItemQnA.builder()
                        .title(dto.getTitle())
                        .content(dto.getContent().replaceAll("\\s+", " "))
                        .writer(member.getUserId().substring(0, 3) + "***")
                        .hide(dto.getHide())
                        .replyEmpty(true)
                        .build();
        item.addQnaList(qna);
        member.addQnaList(qna);

        return qnaRepository.save(qna).getId();
    }

    @Override
    @Transactional
    public void deleteAllById(List<Long> id) {
        qnaRepository.deleteAllById(id);
    }


}
