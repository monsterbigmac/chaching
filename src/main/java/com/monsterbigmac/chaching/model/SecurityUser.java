package com.monsterbigmac.chaching.model;

import com.monsterbigmac.chaching.entity.AppUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class SecurityUser implements UserDetails {

    private final AppUser appUser;

    public SecurityUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public Long getAppUserId() {
        return appUser.getId();
    }

    @Override
    public String getUsername() {
        return appUser.getEmail();
    }

    @Override
    public String getPassword() {
        return appUser.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

}
