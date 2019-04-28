package com.mars.monkey.auth.service;

import com.mars.monkey.auth.dao.mapper.AuthMapper;
import com.mars.monkey.security.model.TokenUser;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * Created on 5/8/2019.
 *
 * @author YouFeng Zhu
 */
@Service
public class MMUserDetailService implements UserDetailsService {
    private final static Logger LOGGER = LoggerFactory.getLogger(MMUserDetailService.class);

    @Autowired
    private AuthMapper authMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        TokenUser authUser = getAuthUser(username);
        LOGGER.debug("Found auth user : {}", authUser);
        return authUser;
    }

    private TokenUser getAuthUser(String username) {
        List<Map<String, Object>> result = authMapper.getAuthInfoByUsername(username);
        if (CollectionUtils.isEmpty(result)) {
            throw new UsernameNotFoundException(username);
        }
        Map<String, Object> first = result.get(0);

        List<String> authorities = new LinkedList<>();

        for (Map<String, Object> item : result) {
            if (item.get("role_name") != null) {
                authorities.add(item.get("role_name").toString());
            }
        }
        TokenUser authUser = new TokenUser((Long) first.get("user_id"), User
                .withUsername((String) first.get("username"))
                .password((String) first.get("password"))
                .authorities(authorities.toArray(new String[0]))
                .build());
        return authUser;
    }
}
