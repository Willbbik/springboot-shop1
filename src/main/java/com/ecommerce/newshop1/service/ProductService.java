package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.dto.ProOptDto;
import com.ecommerce.newshop1.dto.ProOptNameDto;
import com.ecommerce.newshop1.entity.ProOptEntity;
import com.ecommerce.newshop1.entity.ProOptNameEntity;
import com.ecommerce.newshop1.entity.ProductEntity;
import com.ecommerce.newshop1.repository.ProOptNameRepository;
import com.ecommerce.newshop1.repository.ProOptRepository;
import com.ecommerce.newshop1.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProOptRepository proOptRepository;
    private final ProOptNameRepository proOptNameRepository;

    String[] values = {"option1", "option2", "option3", "option4", "option5" };

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
                .productEntity(productEntity)
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
                .productEntity(productEntity)
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