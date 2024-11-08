package com.carrotMarket.carrotMarket.NewProject.board.mapper;

import com.carrotMarket.carrotMarket.NewProject.board.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    @Insert("INSERT INTO users (username, password, email, location, latitude, longitude, profile_image, created_at, group_type) " +
            "VALUES (#{username}, #{password}, #{email}, #{location}, #{latitude}, #{longitude}, #{profileImage}, #{createdAt}, #{groupType})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertUser(User user);

    @Select("SELECT * FROM users WHERE id = #{id}")
    User selectUserById(Long id);

    @Select("SELECT * FROM users")
    List<User> selectAllUsers();

    @Update("UPDATE users SET username = #{username}, password = #{password}, email = #{email}, location = #{location}, latitude = #{latitude}, longitude = #{longitude}, profile_image = #{profileImage}, group_type = #{groupType} WHERE id = #{id}")
    void updateUser(User user);

    @Delete("DELETE FROM users WHERE id = #{id}")
    void deleteUser(Long id);
}
