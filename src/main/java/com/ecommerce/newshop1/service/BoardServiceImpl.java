package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.dto.BoardDto;
import com.ecommerce.newshop1.entity.Board;
import com.ecommerce.newshop1.entity.Member;
import com.ecommerce.newshop1.exception.BoardNotFoundException;
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
    private final ModelMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Long count() {
        return boardRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Board findById(Long boardId) {

        return boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException("존재하지 않는 게시물입니다."));
    }

    @Override
    @Transactional
    public Long save(BoardDto boardDto, String userId) {

        Member member = memberService.findByUserId(userId);
        Board board = mapper.map(boardDto, Board.class);
        board.setWriter(member.getUserId().substring(0, 3) + "***");
        member.addBoardList(board);

        return boardRepository.save(board).getId();
    }

    @Override
    @Transactional
    public Long update(Long boardId, BoardDto boardDto) {

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException("존재하지 않는 게시물입니다."));

        board.setTitle(boardDto.getTitle());
        board.setContent(boardDto.getContent());
        board.setHide(boardDto.getHide());
        return board.getId();
    }

    @Override
    @Transactional
    public Long updateHide(Long boardId) {

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException("존재하지 않는 게시물입니다."));
        if(board.getHide().equals("private")){
            board.setHide("public");
        }else{
            board.setHide("private");
        }
        return board.getId();
    }


    @Override
    @Transactional(readOnly = true)
    public List<BoardDto> searchAll(Pageable pageable) {
        return boardRepository.searchAll(pageable);
    }


    @Override
    public BoardDto editBoardDto(BoardDto boardDto) {

        String writer = boardDto.getWriter().substring(0, 3) + "***";
        boardDto.setWriter(writer);

        return boardDto;
    }

    @Override
    @Transactional
    public void deleteById(Long boardId) {
        boardRepository.deleteById(boardId);
    }


}
