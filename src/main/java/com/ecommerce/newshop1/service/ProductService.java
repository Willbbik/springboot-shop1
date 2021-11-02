package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.dto.*;
import com.ecommerce.newshop1.entity.*;
import com.ecommerce.newshop1.repository.*;

import com.ecommerce.newshop1.utils.SecurityService;
import com.ecommerce.newshop1.utils.Options;
import com.ecommerce.newshop1.utils.enums.Role;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProOptRepository proOptRepository;
    private final ProOptNameRepository proOptNameRepository;
    private final QnARepository qnARepository;
    private final SecurityService security;

    String[] values = {"option1", "option2", "option3", "option4", "option5" };

    ModelMapper mapper = new ModelMapper();

    // 옵션이 존재하는지 체크
    @Transactional(readOnly = true)
    public boolean productOptCheck(Long productId){

        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다. productId = " + productId));

        ProOptNameEntity optionName = proOptNameRepository.findByProductId(product);

        return optionName != null;
    }

    // 상품 가져오기
    @Transactional(readOnly = true)
    public ProductDto getProducts(Long productId){

        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다. productId = " + productId));

        ProductDto proDto = mapper.map(product, ProductDto.class);
        return proDto;
    }

    // 상품 옵션 가져오기
    @Transactional(readOnly = true)
    public Model getOptionAndNames(Long productId, Model model) throws Exception {
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다. productId = " + productId));

        // Entity에서 Dto로 형변환
        ProOptNameDto optionNames =
                mapper.map(proOptNameRepository.findByProductId(product), ProOptNameDto.class);

        List<ProOptDto> optDtos = proOptRepository.findAllByProductId(product)
                        .stream().map(p -> mapper.map(p, ProOptDto.class))
                        .collect(Collectors.toList());

        // 상품 옵션
        List<Options> options = overlapRemove(optDtos, 1);

        model.addAttribute("optionNames", optionNames);
        model.addAttribute("options", options);
        return model;
    }


    // 옵션 개수 검사 메소드
    public int proOptLength(ProOptDto proOptDto) throws Exception {

        int cnt = 0;

        // 몇개의 옵션이 입력됐는지
        for (Field field : proOptDto.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            for (String str : values) {
                if (field.getName().equals(str)) {
                    if (field.get(proOptDto) != null) {
                        // 하나라도 있다면 확인했으니 바로 끝내고 1리턴
                        cnt ++;
                        break;
                    }
                }
            }
        }
        return cnt;
    }


    /**
     *  상품 상세보기에서 옵션에 표시될
     *  옵션 중복값 제거
     *
     *  옵션 배열에서 중복을 제거할 필드의 값들을 배열에 다 넣고
     *  set 컬렉션에 담아서 중복 제거하고, 그 결과를 배열에 담고
     *  중복이 제거된 배열을 리턴한다
     *
     *  when 언제 사용하나면
     *   ajax로 옵션값 요청을 받고
     *   옵션값 리턴할 때 사용한다.
     *
     * @param dtos
     * @param paramIndex
     * @return
     * @throws Exception
     */
    public List<Options> overlapRemove(List<ProOptDto> dtos, int paramIndex) throws Exception {

        List<String> list = new ArrayList<>();       // 기존 옵션 값들을 담을 리스트
        int index = paramIndex - 1;                  // 원하는 필드값을 가져오기 위해서

        for (ProOptDto dto : dtos){
            Field field = dto.getClass().getDeclaredField(values[index]); // 특정 옵션 필드값 가져오기
            field.setAccessible(true);

            list.add((String) field.get(dto));
        }

        Set<String> set = new HashSet<>(list);     // 중복 제거
        List<String> result = new ArrayList<>(set);

        List<Options> options = result.stream()
                .map(Options::new)
                .collect(Collectors.toList());
        return options;
    }


    // saveAll 하기 위해서 dto를 entity로 바꾸기

    /**
     * 상품 등록 페이지에서 받은 상품 옵션값들을 저장하기 위해서
     *
     * 파라미터
     *  proOptDto - 옵션명
     *  cnt - 옵션명 개수
     *  productEntity - 상품 번호
     *
     * @param proOptDto
     * @param cnt
     * @param productEntity
     * @return
     * @throws Exception
     */
    public List<ProOptEntity> proOptDtoToEntities(ProOptDto proOptDto, int cnt, ProductEntity productEntity) throws Exception {

        List<ProOptEntity> entities = new ArrayList<ProOptEntity>();                // 마지막에 리턴할 entity List
        int optLength = proOptDto.getOption1().split(",").length;  // 옵션 개수 알기 위해서

        String[][] option = new String[5][optLength+1];                  // 옵션값들 담기 위해서
        String[] stock = proOptDto.getStock().split(",");          // 재고 담기

        // 값이 있는건 값들 담고, 없으면 Null 담기
        // 옵션명은 최대 5개까지만 입력할 수 있어서
        for(int i = 0; i < 5; i++){

            // i가 현재 입력된 옵션명 개수보다 높다면
            if(i+1 > cnt){
                for(int j = 0; j < optLength; j++){
                    option[i][j] = null;
                }
            }
            else {
                // 같은 이름의 필드의 값을 가져온다
                Field field = proOptDto.getClass().getDeclaredField(values[i]);
                field.setAccessible(true);
                String value = (String) field.get(proOptDto);

                // 값이 여러개일수도 있으니 split해서 담는다
                if(value.contains(",")){
                    option[i] = value.split(",");
                } else{
                    option[i][0] = value;
                }
            }
        }

        // entity List에 담아주기
        for(int i = 0; i < optLength; i++){

            ProOptEntity entity = ProOptEntity.builder()
                .productId(productEntity)
                .option1(option[0][i])
                .option2(option[1][i])
                .option3(option[2][i])
                .option4(option[3][i])
                .option5(option[4][i])
                .stock(Integer.parseInt(stock[i]))
                .build();
            entities.add(entity);
        }

        return entities;
    }

    // QnA 개수 가져오기 ( 답글 제외 )
    @Transactional(readOnly = true)
    public int getQnaSize(Long productId){

        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다. productId = " + productId));

        List<QnAEntity> qnaSize = qnARepository.findAllQnASize(product);

        return qnaSize.size();
    }


    // 쿼리스트링으로 들어온 page 유효성 검사 메소드
    public Pageable editPageable(int page, int qnaSize){

        // 존재하는 페이지보다 높은 페이지가 들어오거나
        // 0보다 작다면
        // 기본 0으로 설정
        // 아니라면 들어온 값 그대로 사용

        int maxPage = (int) Math.round((qnaSize * 1.0) / 3);

        if(page < maxPage && page >= 0){ }
        else page = 0;

        return PageRequest.of(page, 3, Sort.by("createdDate").descending());
    }


    @Transactional
    public void saveProduct(ProductEntity productEntity){
         productRepository.save(productEntity);
    }

    @Transactional
    public void saveProOptions(List<ProOptEntity> proOptEntities){
         proOptRepository.saveAll(proOptEntities);
    }

    @Transactional
    public void saveProOptName(ProOptNameDto dto, ProductEntity productEntity){

        ProOptNameEntity entity = ProOptNameEntity.builder()
                .productId(productEntity)
                .optionName1(dto.getOptionName1())
                .optionName2(dto.getOptionName2())
                .optionName3(dto.getOptionName3())
                .optionName4(dto.getOptionName4())
                .optionName5(dto.getOptionName5())
                .build();

        proOptNameRepository.save(entity);
    }

    // QnA 유효성 검사

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
    public int qnaValidationCheck(QnADto dto){

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
    public void saveQnaAnswer(QnADto dto){

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
    public List<QnADto> qnaEdit(ProductEntity productId, Pageable pageable) throws Exception {

        // qna 가져오기
        List<QnAEntity> qnaEntities = qnARepository.findAllQnA(productId, pageable);

        // qna가 존재한다면
        if (!qnaEntities.isEmpty()) {
            List<QnADto> qnaDtos = qnaEntities.stream()
                    .map(p -> mapper.map(p, QnADto.class))
                    .collect(Collectors.toList());

            // 값들 수정해주기
            for (QnADto dto : qnaDtos) {

                // 제목 길이 설정
                // 제목 : 질문 클릭 안했을 때 처음에 보이는 질문 내용
                if(dto.getContent().length() > 30){
                    dto.setTitle(dto.getContent().substring(0, 30) + "...");
                }else{
                    dto.setTitle(dto.getContent());
                }

                // 비공개 글 설정
                if (dto.getHide().equals("private")) {

                    // 작성자 && 권한 체크
                    if (!security.compareName(dto.getWriter()) &&
                            !security.checkHasRole(Role.ADMIN.getValue())) {
                        dto.setContent("비밀글입니다.");
                        dto.setTitle("비밀글입니다.");
                    }
                }

                // 아이디 마스킹
                dto.setWriter(dto.getWriter().substring(0, 3) + "***");

                // 해당 qna의 답글 가져오기
                Optional<QnAEntity> reply = qnARepository.findByParent(dto.getId());

                // 답글 유무 확인
                if (reply.isPresent()) dto.setReplyEmpty("답변완료");
                else dto.setReplyEmpty("답변대기");

            }
            return qnaDtos;
        }
        else{ // QnA가 없다면 null 리턴
            return null;
        }
    }


    // QnA 답글 가져오는 메소드
    public List<QnADto> getQnAReply(List<QnADto> qnaList) {

        List<QnADto> replyList = new ArrayList<>();

        if(qnaList != null) {

            for (QnADto dto : qnaList) {
                Optional<QnAEntity> entity = qnARepository.findByParent(dto.getId());

                // 존재유무 확인
                if (entity.isPresent()) {
                    replyList.add(mapper.map(entity.get(), QnADto.class));
                } else {
                    QnADto reply = null;
                    replyList.add(reply);
                }
            }
        }
        return replyList;
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
    public List<QnADto> replyEdit(List<QnADto> replyList){

        List<QnADto> replyResult = new ArrayList<>();

        // writer, content, createdDate 수정
        for(QnADto reply : replyList){
            if(reply != null){

                Long qnaId = reply.getParent();
                QnAEntity parent = qnARepository.findById(qnaId)
                        .orElseThrow(() -> new IllegalArgumentException("해당 QnA가 존재하지 않습니다. QnA Id = " + qnaId));

                String parentWriter = parent.getWriter();

                // 작성자 이름 수정
                reply.setWriter("판매자");

                // 비밀글인데 작성자가 아니거나 관리자가 아니면 내용 숨기기
                if(reply.getHide().equals("private")){
                    if(!security.compareName(parentWriter) &&
                            !security.checkHasRole(Role.ADMIN.getValue())){
                        reply.setContent("비밀글입니다.");
                    }
                }
            }else{
                reply = null;
            }
            replyResult.add(reply);
        }

        return replyResult;
    }


    @ApiOperation(value = "QnAList 가져오기")
    /**
     *
     * QnA, QnA답글, QnA 개수를 모델에 담아서
     * html을 반환한다.
     *
     * 상품 상세보기에 QnA칸에 사용된다
     *
     * return html page
     */
    public String getQnAList(Long productId, Model model, int page) throws Exception {

        // 상품번호로 상품 entity 가져오기
        Optional<ProductEntity> entity = productRepository.findById(productId);

        // 댓글 총 개수
        int qnaSize = getQnaSize(productId);

        // QnA와 답글 가져와서 model에 담아주기
        Pageable pageable = editPageable(page, qnaSize);
        List<QnADto> qnaList   = qnaEdit(entity.get(), pageable);
        List<QnADto> replyList = replyEdit(getQnAReply(qnaList));

        model.addAttribute("qnaSize", qnaSize);
        model.addAttribute("qnaList", qnaList);
        model.addAttribute("replyList", replyList);

        return "product/tab/tab3QnA";
    }



}