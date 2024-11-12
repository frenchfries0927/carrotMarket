package com.carrotMarket.carrotMarket.NewProject.board.controller;

import com.carrotMarket.carrotMarket.NewProject.board.entity.ItemBoard;
import com.carrotMarket.carrotMarket.NewProject.board.service.ItemBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/board")
public class ItemBoardController {

    private final ItemBoardService itemBoardService;

    @Autowired
    public ItemBoardController(ItemBoardService itemBoardService) {

        this.itemBoardService = itemBoardService;
    }

    // 상품 목록 페이지
    @GetMapping("/list")
    public String getItemBoardList(Model model) {
        List<ItemBoard> items = itemBoardService.getAvailableItems();
        model.addAttribute("items", items);
        return "list";  // 템플릿 파일이 "resources/templates/list.html"에 있으므로 경로를 "list"로 수정
    }

    // 상품 상세 페이지
    @GetMapping("/detail/{id}")
    public String getItemDetail(@PathVariable Long id, Model model) {
        ItemBoard item = itemBoardService.getItemById(id);
        model.addAttribute("item", item);
        return "detail";  // 템플릿 파일이 "resources/templates/detail.html"에 있으므로 경로를 "detail"로 수정
    }
}
