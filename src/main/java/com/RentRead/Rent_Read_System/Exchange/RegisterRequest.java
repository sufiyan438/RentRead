package com.RentRead.Rent_Read_System.Exchange;

import lombok.Data;
import lombok.NoArgsConstructor;

import com.RentRead.Rent_Read_System.DTO.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest {
    @NotNull
    private String name;

    @NotNull
    @Email(message="Invalid email address!")
    private String email;

    @NotNull
    private String password;

    private Role role;
    // private String role;
}
