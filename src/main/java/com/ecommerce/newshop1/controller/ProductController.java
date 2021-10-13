package com.ecommerce.newshop1.controller;

import com.ecommerce.newshop1.dto.ProductDto;
import com.ecommerce.newshop1.dto.ProOptDto;
import com.ecommerce.newshop1.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;


@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // 상품 등록 페이지
    @GetMapping("/admin/product/reg")
    public String productAdminPage() {

        return "product/registration";
    }

    @PostMapping("/product/register")
    public String register(ProOptDto proOptDto, ProductDto productDto) throws Exception {

        // @RequestParam(value = "option1") String[] option1

        int cnt = productService.proOptLength(proOptDto);


//        if(cnt == 0) {      // 옵션이 없을 때
//            // 상품 저장
//        } else if(cnt > 0){ // 옵션이 있을 때
//            // 상품 저장
//            // 상품 옵션 저장
//            // 상품 옵션 이름 저장
//        }

        productService.proOptDtoToEntities(proOptDto, cnt);


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
