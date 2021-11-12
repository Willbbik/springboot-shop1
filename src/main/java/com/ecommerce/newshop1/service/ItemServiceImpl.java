package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.dto.ItemDto;
import com.ecommerce.newshop1.dto.SearchDto;
import com.ecommerce.newshop1.entity.Item;
import com.ecommerce.newshop1.repository.ItemRepository;
import com.ecommerce.newshop1.repository.ItemRepositoryImpl;
import com.ecommerce.newshop1.utils.ItemPagination;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    // private final ItemRepository itemRepository;
    private final ItemRepositoryImpl itemRepository;



    @Transactional(readOnly = true)
    @Override
    public List<ItemDto> searchAll(SearchDto searchDto, Pageable pageable){
        return itemRepository.searchAll(searchDto, pageable);
    }


    @Override
    public Long searchTotal(SearchDto searchDto) {

        return itemRepository.searchTotal(searchDto);
    }


    @Override
    @Transactional(readOnly = true)
    @ApiOperation(value = "ajax 전용", notes = "페이징 버튼 클릭시 비동기로 상품들쪽 html만 바꿔준다")
    public String getHtmlItemList(int page, SearchDto searchDto, Model model) {

        // 상품 총 개수
        Long totalElements = searchTotal(searchDto);

        // 페이징기능. 상품 개수와 현재 페이지 저장 후 계산
        ItemPagination pagination = new ItemPagination();
        pagination.setTotalPost(totalElements);
        pagination.setCurPage(page);
        pagination.calculate();
        int curPage = pagination.getCurPage();

        // 상품 가져오기
        Pageable pageable = PageRequest.of(curPage - 1, 10, Sort.Direction.DESC, "createdDate");
        List<ItemDto> items = searchAll(searchDto, pageable);

        model.addAttribute("page", pagination);
        model.addAttribute("curPage", curPage);
        model.addAttribute("startPage", pagination.getStartPage());
        model.addAttribute("endPage", pagination.getEndPage());

        model.addAttribute("items", items);
        model.addAttribute("itemName", searchDto.getItemName());
        model.addAttribute("category", searchDto.getCategory());
        model.addAttribute("saleStatus", searchDto.getSaleStatus());

        return "admin/admin_replaceItemList";
    }



}
