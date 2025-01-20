package com.RentRead.Rent_Read_System.Exchange;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
public class AuthResponse {
    private final String message = "Success";
}
