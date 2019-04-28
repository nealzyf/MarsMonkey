package com.mars.monkey.security.model;

import java.util.ArrayList;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Created on 5/9/2019.
 *
 * @author YouFeng Zhu
 */
public class TokenUser extends User {

    private Long userId;


    public TokenUser(Long userId, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.userId = userId;
    }

    public TokenUser(Long userId, UserDetails user) {
        super(user.getUsername(), user.getPassword(), user.getAuthorities());
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }


}
