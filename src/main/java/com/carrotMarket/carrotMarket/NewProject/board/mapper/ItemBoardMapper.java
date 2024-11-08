package com.carrotMarket.carrotMarket.NewProject.board.mapper;

import com.carrotMarket.carrotMarket.NewProject.board.entity.ItemBoard;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ItemBoardMapper {

    @Insert("INSERT INTO item_board (title, description, price, category, created_at, user_id, item_image, status) " +
            "VALUES (#{title}, #{description}, #{price}, #{category}, #{createdAt}, #{userId}, #{itemImage}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertItemBoard(ItemBoard itemBoard);

    @Select("SELECT * FROM item_board WHERE id = #{id}")
    ItemBoard selectItemBoardById(Long id);

    @Select("SELECT * FROM item_board")
    List<ItemBoard> selectAllItemBoards();

    @Update("UPDATE item_board SET title = #{title}, description = #{description}, price = #{price}, category = #{category}, status = #{status}, item_image = #{itemImage} WHERE id = #{id}")
    void updateItemBoard(ItemBoard itemBoard);

    @Delete("DELETE FROM item_board WHERE id = #{id}")
    void deleteItemBoard(Long id);
}
