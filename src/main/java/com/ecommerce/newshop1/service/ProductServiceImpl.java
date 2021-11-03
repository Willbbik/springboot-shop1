package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.dto.*;
import com.ecommerce.newshop1.entity.*;
import com.ecommerce.newshop1.repository.*;

import com.ecommerce.newshop1.utils.SecurityService;
import com.ecommerce.newshop1.dto.Options;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProOptRepository proOptRepository;
    private final ProOptNameRepository proOptNameRepository;

    String[] values = {"option1", "option2", "option3", "option4", "option5" };

    ModelMapper mapper = new ModelMapper();

    // 옵션이 존재하는지 체크
    @Transactional(readOnly = true)
    @Override
    public boolean checkProductOption(Long productId){

        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다. productId = " + productId));

        ProOptNameEntity optionName = proOptNameRepository.findByProductId(product);

        return optionName != null;
    }

    // 상품 등록후 저장할 때 옵션 있는지 없는지 확인
    @Override
    public int checkOptionExist(ProOptDto proOptDto) throws Exception {

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

    // 상품 가져오기
    @Transactional(readOnly = true)
    @Override
    public ProductDto getProducts(Long productId){

        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다. productId = " + productId));

        ProductDto proDto = mapper.map(product, ProductDto.class);
        return proDto;
    }

    // 상품 옵션명 가져오기
    @Transactional(readOnly = true)
    @Override
    public ProOptNameDto getOptionName(ProductEntity productId){

        ProOptNameEntity entity = proOptNameRepository.findByProductId(productId);
        return mapper.map(entity, ProOptNameDto.class);
    }

    // 상품 옵션 가져오기
    @Transactional(readOnly = true)
    @Override
    public List<Options> getOptions(ProductEntity productId, int index) throws Exception {

        List<ProOptEntity> entity = proOptRepository.findAllByProductId(productId);

        List<ProOptDto> optDtos = entity.stream().map(p -> mapper.map(p, ProOptDto.class))  // optionEntity > dto
                                .collect(Collectors.toList());

        // 상품 옵션값 중복 제거
        List<Options> options = overlapRemove(optDtos, index);
        return options;

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
    @Override
    public List<Options> overlapRemove(List<ProOptDto> dtos, int paramIndex) throws Exception {

        List<String> list = new ArrayList<>();       // 기존 옵션 값들을 담을 리스트
        int index = paramIndex - 1;                  // 원하는 필드값을 가져오기 위해서

        for (ProOptDto dto : dtos){
            Field field = dto.getClass().getDeclaredField(values[index]); // 특정 옵션 필드값 가져오기
            field.setAccessible(true);

            list.add((String) field.get(dto));     // 리스트에 담기
        }

        Set<String> set = new HashSet<>(list);     // 중복 제거
        List<String> result = new ArrayList<>(set);

        List<Options> options = result.stream()
                .map(Options::new)
                .collect(Collectors.toList());
        return options;
    }



    /**
     * 상품 등록 페이지에서 받은 상품 옵션값들을 저장하기 위해서
     * (saveAll 하기 위해서)
     * dto에서 entity로 형변환
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
    @Override
    public List<ProOptEntity> convertOptions(ProOptDto proOptDto, int cnt, ProductEntity productEntity) throws Exception {

        List<ProOptEntity> entities = new ArrayList<ProOptEntity>();                // 마지막에 리턴할 entity List
        int optLength = proOptDto.getOption1().split(",").length;  // 옵션 개수

        String[][] option = new String[5][optLength+1];                  // 옵션값들 담기 위해서
        String[] stock = proOptDto.getStock().split(",");          // 재고

        // 값이 있으면 담고, 없으면 Null 담기
        // 옵션명은 최대 5개까지만 입력할 수 있어서
        for(int i = 0; i < 5; i++){

            // 옵션 다 저장했을때
            if(i+1 > cnt){
                for(int j = 0; j < optLength; j++){
                    option[i][j] = null;
                }
            }
            else {
                // 같은 이름의 필드의 값을 가져온다
                Field field = proOptDto.getClass().getDeclaredField(values[i]);
                field.setAccessible(true);
                String value = (String) field.get(proOptDto);   // 옵션값

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


    @Transactional
    @Override
    public void saveProduct(ProductEntity productEntity){

        productRepository.save(productEntity);
    }

    @Transactional
    @Override
    public void saveProOptions(List<ProOptEntity> proOptEntities){

        proOptRepository.saveAll(proOptEntities);
    }

    @Transactional
    @Override
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




}