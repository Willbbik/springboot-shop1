package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.dto.BoardDto;
import com.ecommerce.newshop1.entity.Board;
import com.ecommerce.newshop1.entity.Member;
import com.ecommerce.newshop1.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{

    private final BoardRepository boardRepository;
    private final MemberService memberService;

    ModelMapper mapper = new ModelMapper();

    @Override
    @Transactional
    public Long save(BoardDto boardDto, String userId) {

        Member member = memberService.findByUserId(userId);
        Board board = mapper.map(boardDto, Board.class);
        board.setWriter(member.getUserId());
        member.addBoardList(board);

        return boardRepository.save(board).getId();
    }



}
