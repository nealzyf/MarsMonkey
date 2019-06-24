package com.mars.monkey.user.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mars.monkey.user.dao.entity.User;
import java.util.List;

public interface UserMapper extends BaseMapper<User> {
    int deleteByPrimaryKey(Long userId);

    int insert(User record);

    User selectByPrimaryKey(Long userId);

    List<User> selectAll();

    int updateByPrimaryKey(User record);
}