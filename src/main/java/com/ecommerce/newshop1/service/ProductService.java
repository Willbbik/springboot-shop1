package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.dto.ProOptDto;
import com.ecommerce.newshop1.dto.ProOptNameDto;
import com.ecommerce.newshop1.dto.ProductDto;
import com.ecommerce.newshop1.entity.ProOptEntity;
import com.ecommerce.newshop1.entity.ProOptNameEntity;
import com.ecommerce.newshop1.entity.ProductEntity;
import com.ecommerce.newshop1.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private String[][] option;


//    public String insertProducts(HttpServletRequest request, ProductDto productDto) throws Exception {
//
//        List<ProOptEntity> optEntities = new ArrayList<ProOptEntity>();
//
//        int optLength = request.getParameterValues("option1").length;
//        int nameLength = request.getParameterValues("optName").length;
//
//        String[][] option = new String[6][optLength];
//        String[] name = new String[nameLength];
//
//        option[0] = request.getParameterValues("option1");
//        option[1] = request.getParameterValues("option2");
//        option[2] = request.getParameterValues("option3");
//        option[3] = request.getParameterValues("option4");
//        option[4] = request.getParameterValues("option5");
//        String[] stock = request.getParameterValues("stock");
//
//        name = request.getParameterValues("optName");
//
//        Map<ProOptEntity, ProOptNameDto> map = new HashMap<ProOptEntity, ProOptNameDto>();
//
//        // 상품 옵션들 담기
//        for(int i = 0; i < optLength; i++){
//            ProOptEntity entity = ProOptEntity.builder()
//                    .option1(option[0][i])
//                    .option2(option[1][i])
//                    .option3(option[2][i])
//                    .option4(option[3][i])
//                    .option5(option[4][i])
//                    .stock(Integer.parseInt(stock[i]))
//                    .build();
//
//            optEntities.add(entity);
//        }
//
//        // 상품 옵션 이름 담기
//        for(qwe : name){
//
//
//        }
//
//
//
//    }

    // 옵션 개수 검사 메소드
    public int proOptLength(ProOptDto proOptDto) throws Exception {

        String[] values = {"option1", "option2", "option3", "option4", "option5" };
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
    public List<ProOptEntity> proOptDtoToEntities(ProOptDto proOptDto, int cnt) throws Exception {

        String[] values = {"option1", "option2", "option3", "option4", "option5" };

        List<ProOptEntity> entities = new ArrayList<ProOptEntity>();
        int optLength = proOptDto.getOption1().split(",").length;

        String[][] option = new String[5][optLength+1];
        String[] stock = proOptDto.getStock().split(",");

        // 들어온 옵션 수 만큼 배열에 담는다
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
                option[i] = value.split(",");
            }
        }

        // entity List에 담아주기
        for(int i = 0; i < optLength; i++){

            ProOptEntity entity = ProOptEntity.builder()
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