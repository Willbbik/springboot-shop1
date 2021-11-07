package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.dto.*;
import com.ecommerce.newshop1.entity.Item;
import com.ecommerce.newshop1.entity.ItemImage;
import com.ecommerce.newshop1.entity.ProOptEntity;
import com.ecommerce.newshop1.entity.ProductEntity;

import java.util.List;

public interface ProductService {



    // 상품 가져오기
    ProductDto getProducts(Long productId);

    // 상품 옵션명 가져오기
    ProOptNameDto getOptionName(ProductEntity productId);

    // 상품 옵션 가져오기
    List<Options> getOptions(ProductEntity productId, int index) throws Exception;


    // 등록한 상품 저장
    void saveProduct(ProductEntity productEntity);

    // 등록한 상품 옵션 저장
    void saveProOptions(List<ProOptEntity> proOptEntities);

    // 등록한 상품의 옵션명 저장
    void saveProOptName(ProOptNameDto dto, ProductEntity productEntity);

    // 상품 저장
    Long saveItem(Item item);

    // 상품 이미지 저장
    Long saveItemImage(ItemImage itemImage);

    // 상품 옵션 저장
    void saveItemOptions(ItemOptDto itemOptDto, Item item);

    // 상품 등록후 저장할 때 옵션 있는지 없는지 확인
    int checkOptionExist(ProductOptionDto productOptionDto) throws Exception;

    // 상품 상세보기에 옵션 가져올때 옵션 중복값 제거
    List<Options> overlapRemove(List<ProductOptionDto> dtos, int paramIndex) throws Exception;

    // 상품 옵션 저장하기 위해서 dto에서 entity로 형변환
    List<ProOptEntity> convertOptions(ProductOptionDto productOptionDto, int cnt, ProductEntity productEntity) throws Exception;






}
