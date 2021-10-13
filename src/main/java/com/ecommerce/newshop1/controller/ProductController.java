package com.ecommerce.newshop1.controller;

import com.ecommerce.newshop1.dto.ProOptNameDto;
import com.ecommerce.newshop1.dto.ProductDto;
import com.ecommerce.newshop1.dto.ProOptDto;
import com.ecommerce.newshop1.entity.ProOptEntity;
import com.ecommerce.newshop1.entity.ProductEntity;
import com.ecommerce.newshop1.repository.ProOptRepository;
import com.ecommerce.newshop1.repository.ProductRepository;
import com.ecommerce.newshop1.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;


@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductRepository productRepository;
    private final ProOptRepository proOptRepository;

    // 상품 등록 페이지
    @GetMapping("/admin/product/reg")
    public String productAdminPage() {

        return "product/registration";
    }

    @PostMapping("/product/register")
    public String register(ProOptDto optionDto, ProductDto productDto, ProOptNameDto nameDto) throws Exception {

        int cnt = productService.proOptLength(optionDto);

        ProductEntity productEntity = ProductEntity.builder()
                .pageName(productDto.getPageName())
                .productName(productDto.getProductName())
                .productPrice(productDto.getProductPrice())
                .build();

        if(cnt == 0) {      // 옵션이 없을 때
            productService.saveProduct(productEntity);
        } else if(cnt > 0){ // 옵션이 있을 때
            productService.saveProduct(productEntity);
            List<ProOptEntity> proOptEntities = productService.proOptDtoToEntities(optionDto, cnt, productEntity);
            productService.saveProOptions(proOptEntities);
            productService.saveProOptName(nameDto, productEntity);

            // 상품 저장
            // 상품 옵션 저장
            // 상품 옵션 이름 저장
        }

        return "/product/registration";

    }


//    @PostMapping("/product/register")
//    public String register(ProOptDto optDtoList, ProductDto productDto) throws Exception {
//
//        System.out.println(optDtoList.getOption1().length());
//
//        String[] list = optDtoList.getOption1().split(",");
//        String[] list2 = optDtoList.getOption2().split(",");
//
//        System.out.println(list.length);
//        System.out.println(list2.length);
//
//        for(String qwe : list) {
//            System.out.println(qwe);
//        }
//
//
//        return "/product/registration";
//
//    }








}
