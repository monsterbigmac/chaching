package com.monsterbigmac.chaching.controller;

import com.monsterbigmac.chaching.dto.ApiResponse;
import com.monsterbigmac.chaching.dto.AppUserPayload;
import com.monsterbigmac.chaching.entity.AppUser;
import com.monsterbigmac.chaching.model.SecurityUser;
import com.monsterbigmac.chaching.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AppUserService appUserService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Map<String, Object>>> basicAuthToGetCookie(
            @AuthenticationPrincipal SecurityUser securityUser
    ) {
        return ResponseEntity.ok(ApiResponse.success(Map.of("appUserName", securityUser.getUsername())));
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody AppUserPayload appUserPayload) {
        AppUser appUser = appUserService.createAppUser(appUserPayload);

        return new ResponseEntity<>(
                Map.of(
                        "success", true,
                        "appUserId", appUser.getId()
                ),
                HttpStatus.OK
        );
    }

}
