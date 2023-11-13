package com.example.dating.dto.email;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EmailDto {
    private String email;
    private String password;

    public EmailDto(String email) {
        this.email = email;
    }
}
