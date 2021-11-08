package com.ecommerce.newshop1.controller;

import com.ecommerce.newshop1.dto.*;
import com.ecommerce.newshop1.entity.*;
import com.ecommerce.newshop1.repository.*;
import com.ecommerce.newshop1.service.ProductServiceImpl;
import com.ecommerce.newshop1.service.QnAServiceImpl;
import com.ecommerce.newshop1.utils.SecurityService;
import com.ecommerce.newshop1.utils.enums.Role;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;


@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductServiceImpl productServiceImpl;
    private final QnAServiceImpl qnAService;
    private final SecurityService security;
    private final ProductRepository productRepository;

    ModelMapper mapper = new ModelMapper();

    // 상품 등록 페이지
    @GetMapping("/product/register")
    public String productAdminPage() {

        return "product/registration";
    }


    @GetMapping("/product/getOption")
    public @ResponseBody String getOption(Long productId, String value, int nextIndex){

            if(nextIndex > 5 || nextIndex < 1){
                return "N";
            }
            System.out.println(value);
            return "Y";
    }


    @ApiOperation(value = "상품 상세보기", notes = "여기는 상품의 값과 상품옵션만 model에 담아준다.")
    @GetMapping("/products/{id}")
    public String productDetails(@PathVariable Long id, Model model) throws Exception {

        ProductEntity productId = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다. productId = " + id));

        // QnA 개수
        int qnaSize = qnAService.getQnaSize(id);
        ProductDto product = productServiceImpl.getProducts(id);
        List<Options> options = productServiceImpl.getOptions(productId, 1);
        ProOptNameDto optionNames = productServiceImpl.getOptionName(productId);

        model.addAttribute("product", product);  // 상품
        model.addAttribute("qnaSize", qnaSize);  // qna개수
        model.addAttribute("options", options);  // 옵션
        model.addAttribute("optionNames", optionNames); // 옵션명
        model.addAttribute(model);

        return "product/product";
    }



    @ApiOperation(value = "상품 상세보기에 QnA html 리턴")
    @GetMapping("/product/getqnaList")
    public String getQna (@RequestParam("productId") Long productId, Model model,
                          @RequestParam(name = "page", defaultValue = "0") int page) throws Exception {


        return qnAService.getQnAHtml(productId, model, page);
    }


    @ApiOperation(value = "상품 전체 보기")
    @GetMapping("/categories/all")
    public String productListAll(Model model) {

        // 상품 전체 가져오기
        List<ProductEntity> entities = productRepository.findAllByOrderByIdDesc();

        List<ProductDto> productDtos = entities.stream().
                        map(p -> mapper.map(p, ProductDto.class)).
                        collect(Collectors.toList());

        model.addAttribute("products", productDtos);

        return "product/listAll";
    }


    @ApiOperation(value = "상품 등록", notes = "상품과 옵션들 저장")
    @PostMapping("/product/register")
    public String register(ProductOptionDto optionDto, ProductDto productDto, ProOptNameDto nameDto) throws Exception {

        ProductEntity productEntity = ProductEntity.builder()
                .productName(productDto.getProductName())
                .productPrice(productDto.getProductPrice())
                .pageName(productDto.getPageName())
                .build();

        productServiceImpl.saveProduct(productEntity);
        int cnt = productServiceImpl.checkOptionExist(optionDto);

        if(cnt > 0){ // 옵션이 있으면 같이 저장
            List<ProOptEntity> options = productServiceImpl.convertOptions(optionDto, cnt, productEntity);

            productServiceImpl.saveProOptions(options);
            productServiceImpl.saveProOptName(nameDto, productEntity);
        }

        return "/product/registration";
    }

    @ApiOperation(value = "Q&A 저장")
    @PostMapping("/product/qna/send")
    public @ResponseBody String productQnA(QnADto dto) throws Exception {

        int result = qnAService.checkValidationQnA(dto);
        if(result == 0){
            qnAService.saveQnAQuestion(dto);
            return "Y";     // 저장에 성공하면
        }else if(result == -2){
            return "login"; // 비로그인일때
        }else{
            return "N";     // 유효성 검사에 실패했을때
        }
    }

    @ApiOperation(value = "QnA답글 저장")
    @PostMapping("/product/reply/send")
    public @ResponseBody String productQnaReply(QnADto dto) throws Exception {

        if(!security.checkHasRole(Role.ADMIN.getValue())){
            return "N";
        }else{

            // 여기다 답글 저장 메소드
            qnAService.saveQnAAnswer(dto);
            return "Y";
        }
    }



}
