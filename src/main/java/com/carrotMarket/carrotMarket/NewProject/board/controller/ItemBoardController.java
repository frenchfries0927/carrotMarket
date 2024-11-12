package com.carrotMarket.carrotMarket.NewProject.board.controller;

import com.carrotMarket.carrotMarket.NewProject.board.entity.ItemBoard;
import com.carrotMarket.carrotMarket.NewProject.board.entity.User;
import com.carrotMarket.carrotMarket.NewProject.board.service.ItemBoardService;
import com.carrotMarket.carrotMarket.NewProject.board.service.UserService;
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
    private final UserService userService;


    @Autowired
    public ItemBoardController(ItemBoardService itemBoardService, UserService userService) {

        this.itemBoardService = itemBoardService;
        this.userService = userService;
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
        User user = userService.getUserById(item.getUserId()); // 아이템을 작성한 사용자의 정보를 가져옵니다.
        model.addAttribute("item", item);
        model.addAttribute("user", user);
        return "detail";  // 템플릿 파일이 "resources/templates/detail.html"에 있으므로 경로를 "detail"로 수정
    }

}
