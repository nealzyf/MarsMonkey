package com.mars.monkey.auth.dao.mapper;

import com.mars.monkey.auth.dao.entity.User;
import java.util.List;
import java.util.Map;

/**
 * Created on 5/8/2019.
 *
 * @author YouFeng Zhu
 */
public interface AuthMapper {

    User getUserInfoByUsername(String username);

    List<Map<String, Object>> getAuthInfoByUsername(String username);
}
