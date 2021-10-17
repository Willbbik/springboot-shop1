package com.ecommerce.newshop1.controller;

import com.ecommerce.newshop1.dto.ProOptNameDto;
import com.ecommerce.newshop1.dto.ProductDto;
import com.ecommerce.newshop1.dto.ProOptDto;
import com.ecommerce.newshop1.entity.ProOptEntity;
import com.ecommerce.newshop1.entity.ProOptNameEntity;
import com.ecommerce.newshop1.entity.ProductEntity;
import com.ecommerce.newshop1.repository.ProOptNameRepository;
import com.ecommerce.newshop1.repository.ProOptRepository;
import com.ecommerce.newshop1.repository.ProductRepository;
import com.ecommerce.newshop1.service.ProductService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductRepository productRepository;
    private final ProOptRepository proOptRepository;
    private final ProOptNameRepository proOptNameRepository;

    // 상품 등록 페이지
    @GetMapping("/admin/product/reg")
    public String productAdminPage() {

        return "product/registration";
    }

    // 상품 상세보기
    @GetMapping("/products/{id}")
    public String productDetails(@PathVariable Long id, Model model) throws Exception {



        boolean result = productService.productOptCheck(id);

        model = productService.getProducts(result, id, model);
        model.addAttribute(model);

        return "product/product";
    }


    // 상품 전체 보기
    @GetMapping("/categories/all")
    public String productListAll(Model model) {

        List<ProductEntity> entities = productRepository.findAllByOrderByIdDesc();
        ModelMapper mapper = new ModelMapper();

        List<ProductDto> productDtos =
                entities.stream().map(p -> mapper.map(p, ProductDto.class)).collect(Collectors.toList());


        model.addAttribute("products", productDtos);

        return "product/listAll";
    }



    @ApiOperation(value = "상품 등록", notes = "상품과 옵션들 확인하고 저장")
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






}
