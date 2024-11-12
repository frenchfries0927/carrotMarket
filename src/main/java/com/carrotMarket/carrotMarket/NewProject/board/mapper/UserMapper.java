package com.carrotMarket.carrotMarket.NewProject.board.mapper;

import com.carrotMarket.carrotMarket.NewProject.board.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    @Insert("INSERT INTO user (username, password, email, location, latitude, longitude, profileImage, createdAt, userGroup) " +
            "VALUES (#{username}, #{password}, #{email}, #{location}, #{latitude}, #{longitude}, #{profileImage}, #{createdAt}, #{userGroup})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertUser(User user);

    @Select("SELECT * FROM user WHERE id = #{id}")
    User selectUserById(Long id);

    @Select("SELECT * FROM user WHERE email = #{email}")
    User findByEmail(String email);

    @Select("SELECT * FROM user")
    List<User> selectAllUsers();

    @Update("UPDATE user SET username = #{username}, password = #{password}, email = #{email}, location = #{location}, latitude = #{latitude}, longitude = #{longitude}, profileImage = #{profileImage}, userGroup = #{userGroup} WHERE id = #{id}")
    void updateUser(User user);

    @Delete("DELETE FROM user WHERE id = #{id}")
    void deleteUser(Long id);

}
