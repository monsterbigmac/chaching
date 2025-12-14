package com.monsterbigmac.chaching.service;

import com.monsterbigmac.chaching.dto.AppUserPayload;
import com.monsterbigmac.chaching.entity.AppUser;
import com.monsterbigmac.chaching.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AppUserService {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AppUser createAppUser(AppUserPayload appUserPayload) {
        AppUser appUser = AppUser.builder()
                .email(appUserPayload.getEmail())
                .password(passwordEncoder.encode(appUserPayload.getPassword()))
                .description(appUserPayload.getDescription())
                .build();
        return appUserRepository.save(appUser);
    }

}