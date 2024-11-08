package com.carrotMarket.carrotMarket.NewProject.board.mapper;

import com.carrotMarket.carrotMarket.NewProject.board.entity.ItemBoard;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ItemBoardMapper {

    // 판매 중인 상품 목록을 조회합니다.
    @Select("SELECT * FROM item_board WHERE status = '판매중'")
    List<ItemBoard> findAvailableItems();

    // 특정 상품의 상세 정보를 조회합니다.
    @Select("SELECT * FROM item_board WHERE id = #{id}")
    ItemBoard findById(@Param("id") Long id);

    // 새로운 상품을 추가합니다.
    @Insert("INSERT INTO item_board (title, description, price, category, createdAt, userId, itemImage, location, status) " +
            "VALUES (#{title}, #{description}, #{price}, #{category}, #{createdAt}, #{userId}, #{itemImage}, #{location}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(ItemBoard itemBoard);

    // 상품 정보를 업데이트합니다.
    @Update("UPDATE item_board SET title = #{title}, description = #{description}, price = #{price}, " +
            "category = #{category}, itemImage = #{itemImage}, location = #{location}, status = #{status} " +
            "WHERE id = #{id}")
    void update(ItemBoard itemBoard);

    // 특정 상품을 삭제합니다.
    @Delete("DELETE FROM item_board WHERE id = #{id}")
    void deleteById(@Param("id") Long id);
}
