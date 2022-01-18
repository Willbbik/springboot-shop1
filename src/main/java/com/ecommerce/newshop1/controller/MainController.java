package com.ecommerce.newshop1.controller;


import com.ecommerce.newshop1.dto.ItemDto;
import com.ecommerce.newshop1.service.ItemService;
import com.ecommerce.newshop1.utils.Pagination;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final ItemService itemService;

    @GetMapping("/")
    @ApiOperation(value = "웹 기본 페이지")
    public String index() {
        return "index";
    }

    @GetMapping("/search")
    @ApiOperation(value = "검색 결과 페이지")
    public String search(@RequestParam(name = "itemName", required = false) String itemName,
                         @RequestParam(name = "sort", required = false) String sort,
                         @RequestParam(name = "value", required = false) String value,
                         @RequestParam(name = "more", required = false) String more,
                         @RequestParam(name = "page", required = false, defaultValue = "1") int curPage, Model model){

        Long total = itemService.searchTotal(itemName, null, "onsale");

        Pagination pagination = new Pagination(total, curPage, 9, 10);
        Pageable pageable = PageRequest.of(pagination.getCurPage() - 1, pagination.getShowMaxSize());

        List<ItemDto> itemList = itemService.searchAllBySort(itemName, sort, value, pageable);
        value = itemService.getLastId(itemList, sort, value);

        model.addAttribute("itemList", itemList);
        model.addAttribute("value", value);
        model.addAttribute("page", pagination.getCurPage());
        model.addAttribute("total", total);

        if(more != null) return "common/search_more";
        return "common/search";
    }

//    @GetMapping("/search")
//    @ApiOperation(value = "검색 결과 페이지")
//    public String search(@RequestParam(name = "itemName", required = false) String itemName,
//                         @RequestParam(name = "sort", required = false) String sort,
//                         @RequestParam(name = "value", required = false) String value,
//                         @RequestParam(name = "more", required = false) String more,
//                         @RequestParam(name = "more", required = false, defaultValue = "1") int curPage, Model model){
//
//        Long total = itemService.searchTotal(itemName, sort, value);
//        Pagination page = new Pagination(total, curPage, 9, 10);
//
//
//
//
//        Pageable pageable = PageRequest.ofSize(9);
//
//
//        List<ItemDto> itemList = itemService.searchAllBySort(itemName, sort, value, pageable);
//        value = itemService.getLastId(itemList, sort, value);
//
//        model.addAttribute("itemList", itemList);
//        model.addAttribute("value", value);
//        model.addAttribute("page", 1);
//
//        if(more != null) return "common/search_more";
//        return "common/search";
//    }




}
