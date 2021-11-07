package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.dto.QnADto;
import com.ecommerce.newshop1.entity.ProductEntity;
import com.ecommerce.newshop1.entity.QnAEntity;
import com.ecommerce.newshop1.repository.ProductRepository;
import com.ecommerce.newshop1.repository.QnARepository;
import com.ecommerce.newshop1.utils.SecurityService;
import com.ecommerce.newshop1.utils.enums.Role;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    private final ProductRepository productRepository;
    private final QnARepository qnARepository;
    private final SecurityService security;

    ModelMapper mapper = new ModelMapper();

    /**
     *
     * QnA, QnA답글, QnA 개수를 모델에 담아서
     * html을 반환한다.
     *
     * 상품 상세보기에 QnA칸에 사용된다
     *
     * return html page
     */
    @Override
    public String getQnAHtml(Long productId, Model model, int page) throws Exception {

        Optional<ProductEntity> entity = productRepository.findById(productId);
        int qnaSize = getQnaSize(productId);  // 댓글 총 개수

        // QnA와 답글 가져와서 model에 담아주기
        Pageable pageable = editQnAPageable(page, qnaSize);
        List<QnADto> qnaList = editQna(entity.get(), pageable);     // QnA 편집
        List<QnADto> replyList = editReply(getQnAReply(qnaList));   // QnA 답글 편집

        model.addAttribute("qnaSize", qnaSize);
        model.addAttribute("qnaList", qnaList);
        model.addAttribute("replyList", replyList);

        return "product/tab/tab3QnA";
    }


    // QnA 개수 가져오기 ( 답글 제외 )
    @Transactional(readOnly = true)
    @Override
    public int getQnaSize(Long productId){

        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다. productId = " + productId));

        List<QnAEntity> qnaSize = qnARepository.findAllQnASize(product);

        return qnaSize.size();
    }

    /**
     *
     * view에 표시할 QnA 질문에 대한 답글들 내용 수정 메소드
     *
     * writer, content, createdDate 수정
     *
     * @param replyList
     * @return
     */
    @Override
    public List<QnADto> editReply(List<QnADto> replyList){

        List<QnADto> replyResult = new ArrayList<>();

        // writer, content 수정
        for(QnADto reply : replyList){
            if(reply != null){

                QnAEntity parent = qnARepository.findById(reply.getParent())
                        .orElseThrow(() -> new IllegalArgumentException("해당 QnA가 존재하지 않습니다. QnA Id = " + reply.getParent()));

                if(reply.getHide().equals("private")){
                    if(!security.compareName(parent.getWriter()) && !security.checkHasRole(Role.ADMIN.getValue())){
                        reply.setContent("비밀글입니다.");
                    }
                }

                reply.setWriter("판매자");
            }
                replyResult.add(reply);
        }

        return replyResult;
    }


    // QnA 답글 가져오는 메소드
    @Override
    public List<QnADto> getQnAReply(List<QnADto> qnaList) {

        List<QnADto> replyList = new ArrayList<>();

        if(qnaList != null) {

            for (QnADto dto : qnaList) {
                Optional<QnAEntity> reply = qnARepository.findByParent(dto.getId());

                // 존재유무 확인
                if (reply.isPresent()) {
                    replyList.add(mapper.map(reply.get(), QnADto.class));
                } else {
//                    QnADto reply = null;
                    replyList.add(null);
                }
            }
        }
        return replyList;
    }

    /**
     *  view에 표시할 QnA질문 값들 수정해주는 메소드
     *
     *  파라미터 pageable의 기본 size는 3,
     *  page는 0이지만 바뀔 수 있음
     *
     *
     *  사용자 아이디 마스킹,
     *  qnaDto의 hide가 private일 때 사용자 아이디가 다르거나 권한이 ADMIN이 아니라면 비공개로 내용을 바꾸고,
     *  답글이 존재하는지 확인하고 있다면 replyEmpty의 값을 present로 바꾼다.
     *
     * @param pageable
     * @return List<QnADto>
     */
    @Override
    public List<QnADto> editQna(ProductEntity productId, Pageable pageable) throws Exception {

        List<QnAEntity> qnaEntities = qnARepository.findAllQnA(productId, pageable);

        if (!qnaEntities.isEmpty()) {
            List<QnADto> qnaDtos =
                     qnaEntities.stream()
                    .map(p -> mapper.map(p, QnADto.class))
                    .collect(Collectors.toList());

            // 값들 수정해주기
            for (QnADto dto : qnaDtos) {

                if(dto.getContent().length() > 30){
                    dto.setTitle(dto.getContent().substring(0, 30) + "...");
                } else { dto.setTitle(dto.getContent()); }

                if (dto.getHide().equals("private")) {
                    if (!security.compareName(dto.getWriter()) && !security.checkHasRole(Role.ADMIN.getValue())) {
                        dto.setContent("비밀글입니다.");
                        dto.setTitle("비밀글입니다.");
                    }
                }

                // 해당 qna의 답글 가져오기
                Optional<QnAEntity> reply = qnARepository.findByParent(dto.getId());

                // 답글 유무 확인
                if (reply.isPresent()) dto.setReplyEmpty("답변완료");
                else dto.setReplyEmpty("답변대기");

                dto.setWriter(dto.getWriter().substring(0, 3) + "***");
            }
            return qnaDtos;
        }
        else{ // QnA가 없다면 null 리턴
            return null;
        }
    }

    /**
     * ajax로 값을 받아서 저장
     *
     * 파라미터 qnaDto의 담겨져 온 값들 :
     *  productId, content, parent, hide
     *
     *  qnaEntity에서 set해줘야 하는것들 :
     *  productId, content, parent, hide, depth, writer
     *
     * 부모 QnA에서 수정해줘야 하는 값들 :
     *  replyEmpty - present로 수정
     *
     *
     *@param dto
     */
    @Transactional
    @Override
    public void saveQnAAnswer(QnADto dto){

        ProductEntity product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다. productId = " + dto.getProductId()));

        QnAEntity qnaAnswer = QnAEntity.builder()
                .productId(product)
                .content(dto.getContent())
                .parent(dto.getParent())
                .depth(2)
                .writer(security.getName())
                .hide("private") // 고쳐야함
                .replyEmpty("empty")
                .build();

        qnARepository.save(qnaAnswer);

        // QnA수정 메소드 있어야함
    }

    /**
     * QnA 질문 저장 메소드
     *
     * content - 띄어쓰기 조절
     * parent - 질문은 부모 댓글이 없기 때문에 항상 null
     * depth - QnA 질문은 1, QnA 답변은 2
     * replyEmpty - 등록하자마자는 무조건 답변이 없기때문에 기본으로 empty
     *
     * @param dto
     * @throws Exception
     */
    @Transactional
    @Override
    public void saveQnAQuestion(QnADto dto) throws Exception {

        ProductEntity productEntity = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다. productId = " + dto.getProductId()));

        QnAEntity qnaEntity = QnAEntity.builder()
                .productId(productEntity)
                .writer(security.getName())
                .content(dto.getContent().replaceAll("\\s+", " "))
                .hide(dto.getHide())
                .parent(null)
                .depth(1)
                .replyEmpty("empty")
                .build();

        qnARepository.save(qnaEntity);
    }

    // 쿼리스트링으로 들어온 page 유효성 검사 메소드
    @Override
    public Pageable editQnAPageable(int page, int qnaSize){

        int maxPage = (int) Math.round((qnaSize * 1.0) / 3);

        if(page < maxPage && page >= 0){ }
        else page = 0;

        return PageRequest.of(page, 3, Sort.by("createdDate").descending());
    }

    /**
     * QnA 유효성 검사
     *
     * 값이 없거나, 길이가 정해진 길이보다 길면 -1 리턴
     * 공개, 비공개 값이 private, public가 아닌 다른 값이 들어오면 -1 리턴
     * 권한이 없으면 -2 리턴
     *
     * 정상적이라면 0 리턴
     *
     * @param dto
     * @return
     */
    @Override
    public int checkValidationQnA(QnADto dto){

        String content = dto.getContent();

        if(content.isEmpty() || content.isBlank() || content.length() > 2048){
            return -1;
        }
        if(!security.isAuthenticated()){
            return -2;
        }

        if(dto.getHide().equals("private")) return 0;
        else if(dto.getHide().equals("public")) return 0;
        else return -1;
    }


}
