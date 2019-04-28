package com.mars.monkey.security.model;

/**
 * Created on 5/15/2019.
 *
 * @author YouFeng Zhu
 */
public class AuthMMUser {
    private Long userId;
    private String username;

    public AuthMMUser() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
