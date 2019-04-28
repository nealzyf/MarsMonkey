package com.mars.monkey.user.service;

import com.mars.monkey.user.dao.entity.User;
import com.mars.monkey.user.dao.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created on 4/18/2019.
 *
 * @author eyoufzh
 */
@Service
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserMapper userMapper;

    //    @Transactional
    public int register(User user) {
        return userMapper.insert(user);
    }

    public User getUserInfo(Long userId) {
        return userMapper.selectByPrimaryKey(userId);
    }

}
