package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.dto.BoardDto;
import com.ecommerce.newshop1.entity.Board;
import com.ecommerce.newshop1.entity.Member;
import com.ecommerce.newshop1.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{

    private final BoardRepository boardRepository;
    private final MemberService memberService;

    ModelMapper mapper = new ModelMapper();

    @Override
    @Transactional(readOnly = true)
    public Long count() {
        return boardRepository.count();
    }

    @Override
    @Transactional
    public Long save(BoardDto boardDto, String userId) {

        Member member = memberService.findByUserId(userId);
        Board board = mapper.map(boardDto, Board.class);
        board.setWriter(member.getUserId());
        member.addBoardList(board);

        return boardRepository.save(board).getId();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BoardDto> searchAll(Pageable pageable) {
        return boardRepository.searchAll(pageable);
    }




}
