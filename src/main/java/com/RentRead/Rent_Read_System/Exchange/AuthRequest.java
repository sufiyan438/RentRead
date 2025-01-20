package com.RentRead.Rent_Read_System.Exchange;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class AuthRequest {
    @NotNull
    private String email;
    
    @NotNull
    private String password;
}
