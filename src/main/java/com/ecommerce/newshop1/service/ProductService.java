package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.dto.*;
import com.ecommerce.newshop1.entity.*;
import com.ecommerce.newshop1.repository.*;

import com.ecommerce.newshop1.utils.CommonService;
import com.ecommerce.newshop1.utils.Options;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final CommonService commonService;

    String[] values = {"option1", "option2", "option3", "option4", "option5" };
    ModelMapper mapper = new ModelMapper();

    // 옵션이 존재하는지 체크
    @Transactional(readOnly = true)
    public boolean productOptCheck(Long id){

        Optional<ProductEntity> productEntity = productRepository.findById(id);
        ProOptNameEntity result = proOptNameRepository.findByProductId(productEntity.get());

        return result != null;
    }


    // 옵션 체크하고 있으면 옵션까지, 없으면 상품만 리턴
    @Transactional(readOnly = true)
    public Model getProducts(boolean result, Long id, Model model) throws Exception {

        Optional<ProductEntity> productEntity = productRepository.findById(id);
        ProductEntity entity = productEntity.get();

        if(result){  // 옵션이 있는 상품이라면 옵션까지 리턴

            // Entity에서 Dto로 변환
            ProductDto product = mapper.map(entity, ProductDto.class);
            ProOptNameDto optionNames =
                    mapper.map(proOptNameRepository.findByProductId(entity), ProOptNameDto.class);
            List<ProOptDto> dtos =
                    proOptRepository.findAllByProductId(entity)
                            .stream().map(p -> mapper.map(p, ProOptDto.class))
                            .collect(Collectors.toList());

            // 같은 이름의 옵션값 중복 제거
            List<Options> options = overlapRemove(dtos, 1);

            model.addAttribute("product", product);
            model.addAttribute("optionNames", optionNames);
            model.addAttribute("options", options);

        }else{      // 옵션이 없는 상품이라면 기본 상품정보만 리턴
            ProductDto product = mapper.map(entity, ProductDto.class);
            model.addAttribute("product", product);
        }
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


    // 옵션 중복값 제거
    // 로직 흐름은 dto list중에서 중복을 제거할 필드의 값들을 배열에 다 넣고
    // set 컬렉션에 담아서 중복 제거하고, 그 결과를 배열에 담고
    // 중복이 제거된 배열을 리턴한다

    /**
     *  옵션 중복값 제거
     *  dto list중에서 중복을 제거할 필드의 값들을 배열에 다 넣고
     *  set 컬렉션에 담아서 중복 제거하고, 그 결과를 배열에 담고
     *  중복이 제거된 배열을 리턴한다
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
    public List<ProOptEntity> proOptDtoToEntities(ProOptDto proOptDto, int cnt, ProductEntity productEntity) throws Exception {

        List<ProOptEntity> entities = new ArrayList<ProOptEntity>();     // 마지막에 리턴할 entity List
        int optLength = proOptDto.getOption1().split(",").length;  // 옵션 개수 알기 위해서

        String[][] option = new String[5][optLength+1];                  // 옵션값들 담기 위해서
        String[] stock = proOptDto.getStock().split(",");          // 재고 담기

        // 값이 있는건 값들 담고, 없으면 Null 담기
        for(int i = 0; i < 5; i++){

            if(i+1 > cnt){
                for(int j = 0; j < optLength; j++){
                    option[i][j] = null;
                }
            }else {
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

    @Transactional(readOnly = true)
    public int getQnaSize(Long id){

        Optional<ProductEntity> entity = productRepository.findById(id);
        List<QnAEntity> qnaSize = qnARepository.findAllByProductId(entity.get());
        return qnaSize.size();
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
    public int qnaValidationCheck(QnADto dto){

        String content = dto.getContent();

        if(content.isEmpty() || content.isBlank() || content.length() > 2048){
            return -1;
        }

        if(!commonService.isAuthenticated()){
            return -2;
        }

        if(dto.getHide().equals("private")) return 0;
        else if(dto.getHide().equals("public")) return 0;
        else return -1;

    }


    @Transactional
    public void saveQnAQuestion(QnADto dto) throws Exception {

        String writer = SecurityContextHolder.getContext().getAuthentication().getName();
        ProductEntity productEntity = productRepository.findById(dto.getProductId()).orElseThrow(Exception::new);

        QnAEntity qnaEntity = QnAEntity.builder()
                .productId(productEntity)
                .writer(writer)
                .content(dto.getContent().replaceAll("\\s+", " "))
                .hide(dto.getHide())
                .parent(null)
                .depth(1)
                .replyEmpty("empty")
                .build();

        qnARepository.save(qnaEntity);
    }



    /**
     *  qna 값 설정
     *
     *  파라미터 pageable의 기본 size는 3이고 page는 0이다.
     *  사용자 아이디를 마스킹해주고,
     *  qnaDto의 hide가 private일때 사용자 아이디가 다르거나 권한이 ADMIN이 아니라면 비공개 글로 내용을 바꾸고,
     *  답글이 존재하는지 확인하고 있다면 replyEmpty의 값을 바꾼다.
     *
     * @param pageable
     * @return List<QnADto>
     */
    public List<QnADto> qnaEdit(ProductEntity id, Pageable pageable) throws Exception {

        // qna 가져오기
        List<QnAEntity> qnaEntities = qnARepository.findAllByProductId(id, pageable);

        // 사용자 권한 확인하기 위해서
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // qna가 존재한다면
        if (!qnaEntities.isEmpty()) {
            List<QnADto> qnaDtos = qnaEntities.stream()
                    .map(p -> mapper.map(p, QnADto.class))
                    .collect(Collectors.toList());

            // 시큐리티 컨텍스트 홀더에서 사용자 정보 가져오기

            // 값들 수정해주기
            for (QnADto dto : qnaDtos) {
                // 해당 qna의 답글 가져오기
                Optional<QnAEntity> reply = qnARepository.findByParent(dto.getId());


                // 제목 길이 설정
                if(dto.getContent().length() > 30){
                    dto.setTitle(dto.getContent().substring(0, 30) + "...");
                }else{
                    dto.setTitle(dto.getContent());
                }

                // 비공개 글 설정
                if (dto.getHide().equals("private")) {

                    // 작성자가 본인이 아니거나 관리자가 아닐경우 비밀글로 보이게끔
                    if (!auth.getName().equals(dto.getWriter()) &&
                            !auth.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
                        dto.setContent("비밀글입니다.");
                        dto.setTitle("비밀글입니다.");
                    }
                }

                // 아이디 마스킹
                dto.setWriter(dto.getWriter().substring(0, 3) + "***");

                // 답글 유무 확인
                if (reply.isPresent()) dto.setReplyEmpty("답변완료");
                else dto.setReplyEmpty("답변대기");

            }
            return qnaDtos;
        }
        else{

            List<QnADto> dtos = null;
            return dtos;
        }
    }


    // QnA 답글 가져오는 메소드
    public List<QnADto> getQnAReply(List<QnADto> qnaList) {

        List<QnADto> replyList = new ArrayList<>();

        if(qnaList != null) {

            for (QnADto dto : qnaList) {

                Optional<QnAEntity> entity = qnARepository.findByParent(dto.getId());

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


    // 답글 값 설정 메소드
    public List<QnADto> replyEdit(List<QnADto> replyList){

        List<QnADto> replyResult = new ArrayList<>();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // writer, content, createdDate 수정
        for(QnADto reply : replyList){

            if(reply != null){

                QnAEntity parent = qnARepository.findByParent(reply.getParent()).get();
                String parentWriter = parent.getWriter();

                // 작성자 이름 수정
                reply.setWriter("판매자");

                // 비밀글인데 작성자가 아니거나 관리자가 아니면 내용 숨기기
                if(reply.getHide().equals("private")){

                    if(!auth.getName().equals(parentWriter) &&
                            !auth.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))){
                        reply.setContent("비밀글입니다.");
                    }
                }
            }
            replyResult.add(reply);
        }

        return replyResult;
    }



}