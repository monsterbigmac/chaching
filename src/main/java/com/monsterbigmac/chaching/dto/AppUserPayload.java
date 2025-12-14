package com.monsterbigmac.chaching.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AppUserPayload {

    private String email;

    private String password;

    private String description;

}
