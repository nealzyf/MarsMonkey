package com.mars.monkey.auth.dao.mapper;

import com.mars.monkey.auth.dao.entity.UserRole;
import java.util.List;

public interface UserRoleMapper {
    int deleteByPrimaryKey(Long userRoleId);

    int insert(UserRole record);

    UserRole selectByPrimaryKey(Long userRoleId);

    List<UserRole> selectAll();

    int updateByPrimaryKey(UserRole record);
}