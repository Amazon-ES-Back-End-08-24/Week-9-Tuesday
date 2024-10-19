package com.ironhack.week9tuesday.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;

@Entity
@Table(name = "table-bookings")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate // para que solo se modifique en base de datos lo que actualizamos
// Hibernate: update `table-bookings` set customer_name=? where id=?
// Hibernate: update `table-bookings` set customer_name=?,number_of_guests=?,reservation_date=? where id=?
public class TableBooking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerName;

    private LocalDate reservationDate;

    private Integer numberOfGuests;

    public TableBooking(String customerName, LocalDate reservationDate, Integer numberOfGuests) {
        this.customerName = customerName;
        this.reservationDate = reservationDate;
        this.numberOfGuests = numberOfGuests;
    }
}


