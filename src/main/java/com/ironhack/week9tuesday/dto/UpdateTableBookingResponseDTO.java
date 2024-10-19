package com.ironhack.week9tuesday.dto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTableBookingResponseDTO {

    private Long id;

    private String customerName;

    private LocalDate reservationDate;

    private Integer numberOfGuests;

    // method Entity a DTO
    // method DTO a Entity
}
