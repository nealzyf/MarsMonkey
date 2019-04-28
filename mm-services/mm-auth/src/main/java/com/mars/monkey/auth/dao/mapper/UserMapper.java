package com.mars.monkey.auth.dao.mapper;

import com.mars.monkey.auth.dao.entity.User;
import java.util.List;

public interface UserMapper {
    int deleteByPrimaryKey(Long userId);

    int insert(User record);

    User selectByPrimaryKey(Long userId);

    List<User> selectAll();

    int updateByPrimaryKey(User record);
}