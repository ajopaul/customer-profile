package com.ajopaul.qantas.customerprofile.authentication;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Login {
    @NotNull
    private String username;

    @NotNull
    private String password;

    private String firstName;
    private String lastName;
}
