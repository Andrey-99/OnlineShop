package com.ibs21.OnlineShop.domain;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER, SELLER, ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
