package com.ironhack.week9tuesday.controller;

import com.ironhack.week9tuesday.dto.TableBookingRequestDTO;
import com.ironhack.week9tuesday.dto.TableBookingResponseDTO;
import com.ironhack.week9tuesday.model.TableBooking;
import com.ironhack.week9tuesday.repository.TableBookingRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/table-bookings")
public class TableBookingController {

    @Autowired
    private TableBookingRepository tableBookingRepository;

    @PostMapping
    public ResponseEntity<TableBooking> createBooking(@RequestBody TableBooking tableBooking) {
        TableBooking tableBookingFromDB = tableBookingRepository.save(tableBooking);
        return ResponseEntity.status(HttpStatus.CREATED).body(tableBookingFromDB);
    }

    @PostMapping("/create-dto")
    public ResponseEntity<TableBookingResponseDTO> createBookingWithDTO(@Valid @RequestBody TableBookingRequestDTO tableBookingDTO) {

        TableBooking tableBooking = new TableBooking();

        if (tableBookingDTO.getCustomerName() != null) {
            tableBooking.setCustomerName(tableBookingDTO.getCustomerName().toUpperCase());
        } else {
            tableBooking.setCustomerName("Jane Doe");
        }

        tableBooking.setReservationDate(tableBookingDTO.getReservationDate());
        tableBooking.setNumberOfGuests(tableBookingDTO.getNumberOfGuests());


        // o directamente -> TableBooking tableBooking = new TableBooking(tableBookingDTO.getCustomerName(),tableBookingDTO.getReservationDate(),tableBookingDTO.getNumberOfGuests());


        TableBooking tableBookingFromDB = tableBookingRepository.save(tableBooking);

        TableBookingResponseDTO tableBookingResponseDTO = new TableBookingResponseDTO(tableBookingFromDB.getId(), "HEY NEW BOOKING, THEIR NAME IS " + tableBookingFromDB.getCustomerName());

        return ResponseEntity.status(HttpStatus.CREATED).body(tableBookingResponseDTO);
    }


//    @PostMapping
//    public Void createBooking(@RequestBody TableBooking tableBooking){
//        TableBooking tableBookingFromDB = tableBookingRepository.save(tableBooking);
//        ResponseEntity.created().body(tableBookingFromDB);
//    }


}
