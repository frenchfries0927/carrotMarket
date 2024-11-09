package com.carrotMarket.carrotMarket.NewProject.board.service;


import com.carrotMarket.carrotMarket.NewProject.board.entity.ItemBoard;
import com.carrotMarket.carrotMarket.NewProject.board.mapper.ItemBoardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemBoardService {

    private final ItemBoardMapper itemBoardMapper;

    @Autowired
    public ItemBoardService(ItemBoardMapper itemBoardMapper) {

        this.itemBoardMapper = itemBoardMapper;
    }

    //item list  가져오기
    public List<ItemBoard> getAvailableItems() {

        return itemBoardMapper.findAvailableItems(); // 판매 중인 아이템 리스트를 조회
    }

    // item 상세 조회
    public ItemBoard getItemById(Long id) {

        return itemBoardMapper.findById(id); // 특정 아이템의 상세 정보를 조회
    }
}
