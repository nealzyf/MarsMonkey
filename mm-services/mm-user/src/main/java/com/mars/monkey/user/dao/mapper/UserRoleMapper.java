package com.mars.monkey.user.dao.mapper;

import com.mars.monkey.user.dao.entity.UserRole;
import java.util.List;

public interface UserRoleMapper {
    int deleteByPrimaryKey(Long userRoleId);

    int insert(UserRole record);

    UserRole selectByPrimaryKey(Long userRoleId);

    List<UserRole> selectAll();

    int updateByPrimaryKey(UserRole record);
}