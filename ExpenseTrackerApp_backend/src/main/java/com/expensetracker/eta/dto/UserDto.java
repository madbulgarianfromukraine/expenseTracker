package com.expensetracker.eta.dto;

import com.expensetracker.eta.util.ValidEmail;
import com.expensetracker.eta.util.ValidPassword;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@ValidPassword
public class UserDto {
    @NotNull
    @NotEmpty
    private String username;

    @NotNull
    @NotEmpty
    private String password;
    private String matchingPassword;

    @ValidEmail
    @NotNull
    @NotEmpty
    private String email;

    // standard getters and setters
}