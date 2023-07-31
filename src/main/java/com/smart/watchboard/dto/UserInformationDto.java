package com.smart.watchboard.dto;

import lombok.Getter;

@Getter
public class UserInformationDto {
    private long userId;
    private String nickname;
    private String email;


    public UserInformationDto(long userId, String nickname, String email) {
        this.userId = userId;
        this.nickname = nickname;
        this.email = email;
    }
}
