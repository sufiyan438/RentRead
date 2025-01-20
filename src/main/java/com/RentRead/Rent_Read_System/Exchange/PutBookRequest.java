package com.RentRead.Rent_Read_System.Exchange;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PutBookRequest {
    private String title;

    private String author;

    private String genre;

    // private boolean availibility;
}
