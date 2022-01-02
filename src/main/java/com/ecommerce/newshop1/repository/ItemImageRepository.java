package com.ecommerce.newshop1.repository;

import com.ecommerce.newshop1.entity.ItemImage;
import com.ecommerce.newshop1.repository.custom.ItemImageRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ItemImageRepository extends JpaRepository<ItemImage, Long>, ItemImageRepositoryCustom {

}
