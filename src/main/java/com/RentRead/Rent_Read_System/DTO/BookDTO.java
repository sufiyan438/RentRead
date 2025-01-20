package com.RentRead.Rent_Read_System.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDTO {
    @NotNull
    private String title;

    @NotNull
    private String author;

    @NotNull
    private String genre;

    private boolean availibility = true;
}
