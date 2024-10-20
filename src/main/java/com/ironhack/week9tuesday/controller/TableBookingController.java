package com.ironhack.week9tuesday.controller;

import com.ironhack.week9tuesday.dto.*;
import com.ironhack.week9tuesday.model.TableBooking;
import com.ironhack.week9tuesday.service.TableBookingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/table-bookings")
public class TableBookingController {

    @Autowired
    private TableBookingService tableBookingService;

    @PostMapping
    public ResponseEntity<TableBooking> createBooking(@RequestBody TableBooking tableBooking) {
        TableBooking tableBookingFromDB = tableBookingService.createBooking(tableBooking);
        return ResponseEntity.status(HttpStatus.CREATED).body(tableBookingFromDB);
    }

    @PostMapping("/create-dto")
    public ResponseEntity<CreateTableBookingResponseDTO> createBookingWithDTO(@Valid @RequestBody CreateTableBookingRequestDTO tableBookingDTO) {

        CreateTableBookingResponseDTO responseDTO = tableBookingService.createBookingDto(tableBookingDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }


//    @PostMapping
//    public Void createBooking(@RequestBody TableBooking tableBooking){
//        TableBooking tableBookingFromDB = tableBookingRepository.save(tableBooking);
//        ResponseEntity.created().body(tableBookingFromDB);
//    }

    // preparado para actualizar el recurso entero, en su totalidad
    @PutMapping("/{id}")
    public ResponseEntity<UpdateTableBookingResponseDTO> updateBooking(@PathVariable Long id, @Valid @RequestBody UpdateTableBookingRequestDTO dto) {
        Optional<UpdateTableBookingResponseDTO> optionalTableBooking = tableBookingService.updateBooking(id, dto);

        if (optionalTableBooking.isPresent()) {
            return ResponseEntity.ok(optionalTableBooking.get());
        } else {
            return ResponseEntity.notFound().build();
        }

        // return optionalTableBooking.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // caso en el que el servicio no devuelve un Optional
//    @PutMapping("/{id}")
//    public ResponseEntity<TableBooking> updateBooking(@PathVariable Long id, @RequestBody UpdateTableBookingRequestDTO dto) {
//        TableBooking tableBooking = tableBookingService.updateBooking(id, dto);
//
//        if (tableBooking != null) {
//            return ResponseEntity.ok(tableBooking);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//
//        // return optionalTableBooking.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
//    }

    // preparado para actualizar el recurso parcialmente
    @PatchMapping("/{id}/reservation-date")
    public ResponseEntity<UpdateTableBookingResponseDTO> updateBookingReservationDate(@PathVariable Long id, @RequestBody String reservationDate) {
        Optional<UpdateTableBookingResponseDTO> optionalTableBooking = tableBookingService.updateBookingReservationDate(id, reservationDate);

        if (optionalTableBooking.isPresent()) {
            return ResponseEntity.ok(optionalTableBooking.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/reservation-date-simplified")
    public ResponseEntity<TableBooking> updateBookingReservationDateSimplified(@PathVariable Long id, @RequestBody UpdateReservationDateRequestDTO dto) {
        Optional<TableBooking> optionalTableBooking = tableBookingService.updateBookingReservationDateSimplified(id, dto);

        if (optionalTableBooking.isPresent()) {
            return ResponseEntity.ok(optionalTableBooking.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TableBooking> deleteBooking(@PathVariable Long id) {
        Optional<TableBooking> optionalTableBooking = tableBookingService.deleteBooking(id);

        if (optionalTableBooking.isPresent()) {
            return ResponseEntity.ok(optionalTableBooking.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    // Get vistos Week 8 Tuesday:

    @GetMapping("/{id}")
    public ResponseEntity<TableBooking> getBookingById(@PathVariable("id") Long bookingId) {
        Optional<TableBooking> optionalTableBooking = tableBookingService.findBookingById(bookingId);
        if (optionalTableBooking.isPresent()) {

            TableBooking foundBooking = optionalTableBooking.get();
            return ResponseEntity.ok(foundBooking);

        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<TableBooking>> getAllBookings() {
        List<TableBooking> tableBookings = tableBookingService.findAllBookings();
        if (tableBookings.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(tableBookings);
        }
    }

    // /search?customerName=Pepito
    // /search?reservationDate=2024-11-09
    // /search?customerName=Pepito&reservationDate=2024-11-09
    @GetMapping("/search")
    public List<TableBooking> getBookings(@RequestParam(required = false) String customerName,
                                          @RequestParam(required = false) LocalDate reservationDate) {
        return tableBookingService.findBookings(customerName, reservationDate);
    }

    @GetMapping("/sorted")
    public List<TableBooking> getSortedBookings(@RequestParam String sortBy,
                                                @RequestParam(required = false, defaultValue = "asc") String order) {
        return tableBookingService.findSortedBookings(sortBy, order);
    }
}
