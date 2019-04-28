package com.mars.monkey.auth.dao.mapper;

import com.mars.monkey.auth.dao.entity.Permission;
import java.util.List;

public interface PermissionMapper {
    int deleteByPrimaryKey(Long permissionId);

    int insert(Permission record);

    Permission selectByPrimaryKey(Long permissionId);

    List<Permission> selectAll();

    int updateByPrimaryKey(Permission record);
}