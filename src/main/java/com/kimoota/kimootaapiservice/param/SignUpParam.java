package com.kimoota.kimootaapiservice.param;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class SignUpParam {

    @NotNull
    @Email
    private String email;

    @NotBlank
    private String name;

    @NotBlank
    private String password;
}
