package com.ironhack.week9tuesday.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
// DTO control sobre el código - limit acceso a la entidad
public class UpdateTableBookingRequestDTO {

    private String customerName;

    private LocalDate reservationDate;

    // NO: 0 o números negativos
    @Min(value = 1, message = "At least one guest is required")
    private Integer numberOfGuests;
}