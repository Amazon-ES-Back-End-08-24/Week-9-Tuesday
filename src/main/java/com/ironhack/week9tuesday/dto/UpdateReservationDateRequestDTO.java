package com.ironhack.week9tuesday.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UpdateReservationDateRequestDTO {
    private LocalDate reservationDate;
}
