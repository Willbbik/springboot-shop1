package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.dto.ProOptDto;
import com.ecommerce.newshop1.dto.ProOptNameDto;
import com.ecommerce.newshop1.dto.ProductDto;
import com.ecommerce.newshop1.entity.ProOptEntity;
import com.ecommerce.newshop1.entity.ProOptNameEntity;
import com.ecommerce.newshop1.entity.ProductEntity;
import com.ecommerce.newshop1.repository.ProOptNameRepository;
import com.ecommerce.newshop1.repository.ProOptRepository;
import com.ecommerce.newshop1.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
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

    String[] values = {"option1", "option2", "option3", "option4", "option5" };

    // 옵션이 존재하는지 체크
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public boolean productOptCheck(Long id){
        Optional<ProductEntity> productEntity = productRepository.findById(id);
        ProductEntity entity = productEntity.get();

        ProOptNameEntity result = proOptNameRepository.findByProductId(entity);
        if(result != null){
            return true;
        }else{
            return false;
        }
    }

    // 옵션 체크하고 있으면 옵션까지 없으면 상품만 리턴
    public Model getProducts(boolean result, Long id, Model model) throws Exception {

        ModelMapper mapper = new ModelMapper();

        if(result){  // 옵션이 있다면
            Optional<ProductEntity> productEntity = productRepository.findById(id);
            ProductEntity entity = productEntity.get();
            ProOptNameEntity optNameEntity = proOptNameRepository.findByProductId(entity);
            List<ProOptEntity> optEntities = proOptRepository.findAllByProductId(entity);

            ProductDto product =
                    mapper.map(productEntity, ProductDto.class);
            ProOptNameDto optionNames =
                    mapper.map(optNameEntity, ProOptNameDto.class);
            List<ProOptDto> dtos =
                    optEntities.stream().map(p -> mapper.map(p, ProOptDto.class)).collect(Collectors.toList());


            List<String> options = overlapRemove(dtos, 1);

            model.addAttribute("product", product);
            model.addAttribute("optionNames", optionNames);
            model.addAttribute("options", options);

        }else{      // 옵션이 없다면
            Optional<ProductEntity> entity = productRepository.findById(id);
            ProductDto product =
                    mapper.map(entity, ProductDto.class);
            model.addAttribute("product", product);
        }

        return model;
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


    // 옵션 개수 검사 메소드
    public int proOptLength(ProOptDto proOptDto) throws Exception {

        int cnt = 0;

        // 몇개의 옵션이 입력됐는지
        for (Field field : proOptDto.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            for (String str : values) {
                if (field.getName().equals(str)) {
                    if (field.get(proOptDto) != null) {
                        cnt ++;
                    }
                }
            }
        }

        return cnt;
    }


    // 옵션 중복값 제거
    // 로직 흐름은 dto list중에서 중복을 제거할 필드의 값들을 배열에 다 넣고
    // set 컬렉션에 담아서 중복 제거하고, 그 결과를 일반 배열에 담고
    // 중복이 제거된 일반 배열을 리턴한다
    public List<String> overlapRemove(List<ProOptDto> dtos, int paramIndex) throws Exception {

        List<String> list = new ArrayList<>();       // 기존 옵션 값들을 담을 리스트
        List<ProOptDto> optDtos = new ArrayList<>(); // 리턴할 dto 리스트
        int index = paramIndex - 1;                  // 원하는 필드값을 가져오기 위해서

        for (ProOptDto dto : dtos){
            Field field = dto.getClass().getDeclaredField(values[index]); // 특정 옵션 필드값 가져오기
            field.setAccessible(true);
            list.add((String) field.get(dto));
        }

        Set<String> set = new HashSet<>(list);         // 중복 제거
        List<String> result = new ArrayList<>(set);    // 중복이 제거된 옵션 값들 담기

        // result 개수만큼 dto의 특정
        return result;
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
                // 같은 이름의 필드 변수를 가져온다
                Field field = proOptDto.getClass().getDeclaredField(values[i]);
                field.setAccessible(true);

                // 변수의 값을 가져온다
                String value = (String) field.get(proOptDto);

                // 값이 여러개일 수도 있으니 split해서 담는다
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




}