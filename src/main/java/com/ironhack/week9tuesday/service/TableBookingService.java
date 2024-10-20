package com.ironhack.week9tuesday.service;

import com.ironhack.week9tuesday.dto.*;
import com.ironhack.week9tuesday.model.TableBooking;
import com.ironhack.week9tuesday.repository.TableBookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TableBookingService {

    @Autowired
    private TableBookingRepository tableBookingRepository;

    public CreateTableBookingResponseDTO createBookingDto(CreateTableBookingRequestDTO dto) {
        TableBooking tableBooking = new TableBooking();

        if (dto.getCustomerName() != null) {
            tableBooking.setCustomerName(dto.getCustomerName().toUpperCase());
        } else {
            tableBooking.setCustomerName("Jane Doe");
        }

        tableBooking.setReservationDate(dto.getReservationDate());
        tableBooking.setNumberOfGuests(dto.getNumberOfGuests());


        // o directamente -> TableBooking tableBooking = new TableBooking(tableBookingDTO.getCustomerName(),tableBookingDTO.getReservationDate(),tableBookingDTO.getNumberOfGuests());


        TableBooking tableBookingFromDB = tableBookingRepository.save(tableBooking);

        return new CreateTableBookingResponseDTO(tableBookingFromDB.getId(), "HEY NEW BOOKING, THEIR NAME IS " + tableBookingFromDB.getCustomerName());
    }

    public TableBooking createBooking(TableBooking tableBooking) {

        return tableBookingRepository.save(tableBooking);
    }

    public Optional<UpdateTableBookingResponseDTO> updateBooking(Long id, UpdateTableBookingRequestDTO dto) {
        Optional<TableBooking> optionalTableBooking = tableBookingRepository.findById(id);

        if (optionalTableBooking.isPresent()) {
            TableBooking tableBookingFromDB = optionalTableBooking.get();

            if (dto.getCustomerName() != null) {
                tableBookingFromDB.setCustomerName(dto.getCustomerName());
            }

            if (dto.getReservationDate() != null) {
                tableBookingFromDB.setReservationDate(dto.getReservationDate());
            }

            if (dto.getNumberOfGuests() != null) {
                tableBookingFromDB.setNumberOfGuests(dto.getNumberOfGuests());
            }

            TableBooking updatedTableBooking = tableBookingRepository.save(tableBookingFromDB);

            UpdateTableBookingResponseDTO responseDTO = getResponseDTOFromEntity(updatedTableBooking);

            return Optional.of(responseDTO);
        } else {
            return Optional.empty();
        }
    }

    public Optional<UpdateTableBookingResponseDTO> updateBookingReservationDate(Long id, String reservationDate) {
        Optional<TableBooking> optionalTableBooking = tableBookingRepository.findById(id);

        // todo -> boolean existsInDB = tableBookingRepository.existsById(id);

        if (optionalTableBooking.isPresent()) {
            TableBooking tableBookingFromDB = optionalTableBooking.get();

            if (reservationDate != null) {
                LocalDate parsedReservationDate = LocalDate.parse(reservationDate);
                // try catch - DateTimeParseException

                tableBookingFromDB.setReservationDate(parsedReservationDate);

                TableBooking updatedTableBooking = tableBookingRepository.save(tableBookingFromDB);

                UpdateTableBookingResponseDTO responseDTO = getResponseDTOFromEntity(updatedTableBooking);

                return Optional.of(responseDTO);
            }

            UpdateTableBookingResponseDTO responseDTO = getResponseDTOFromEntity(tableBookingFromDB);

            return Optional.of(responseDTO);

        } else {
            return Optional.empty();
        }
    }

    public Optional<TableBooking> updateBookingReservationDateSimplified(Long id, UpdateReservationDateRequestDTO dto) {

        Optional<TableBooking> optionalTableBooking = tableBookingRepository.findById(id);

        if (optionalTableBooking.isPresent()) {
            TableBooking tableBookingFromDB = optionalTableBooking.get();

            // solo guarda si se actualiza el reservation date
//            if (dto.getReservationDate() != null){
//                tableBookingFromDB.setReservationDate(dto.getReservationDate());
//
//                TableBooking updatedBooking = tableBookingRepository.save(tableBookingFromDB);
//
//                return Optional.of(updatedBooking);
//            }

            // guarda también si no se actualiza el reservation date
            if (dto.getReservationDate() != null) {
                tableBookingFromDB.setReservationDate(dto.getReservationDate());
            }

            TableBooking updatedBooking = tableBookingRepository.save(tableBookingFromDB);

            return Optional.of(updatedBooking);
        } else {
            return Optional.empty();
        }
    }

    public Optional<TableBooking> deleteBooking(Long id) {
        Optional<TableBooking> optionalTableBooking = tableBookingRepository.findById(id);

        if (optionalTableBooking.isPresent()) {
            TableBooking tableBookingFromDB = optionalTableBooking.get();
            // recibe la entity tableBookingRepository.delete(tableBookingFromDB);
            tableBookingRepository.deleteById(id);
            return Optional.of(tableBookingFromDB);
        } else {
            return Optional.empty();
        }
    }

    // method para mapear de un TableBooking a un DTO de respuesta de actualización
    private static UpdateTableBookingResponseDTO getResponseDTOFromEntity(TableBooking tableBooking) {
        UpdateTableBookingResponseDTO responseDTO = new UpdateTableBookingResponseDTO(tableBooking.getId(),
                tableBooking.getCustomerName(), tableBooking.getReservationDate(),
                tableBooking.getNumberOfGuests());
        return responseDTO;
    }


    // methods relacionados a los GET de Week 8 Tuesday:

    public Optional<TableBooking> findBookingById(Long bookingId) {
        // retornamos el optional directamente y en el controlador metemos la lógica relacionada a si está presente o no
        return tableBookingRepository.findById(bookingId);
    }

    public List<TableBooking> findAllBookings() {
        return tableBookingRepository.findAll();
    }

    public List<TableBooking> findBookings(String customerName, LocalDate reservationDate) {
        if (customerName != null && reservationDate != null) {
            return tableBookingRepository.findAllByCustomerNameAndReservationDate(customerName, reservationDate);

        } else if (customerName != null) {
            return tableBookingRepository.findAllByCustomerName(customerName);

        } else if (reservationDate != null) {
            return tableBookingRepository.findAllByReservationDate(reservationDate);

        } else {
            return tableBookingRepository.findAll();
        }
    }

    public List<TableBooking> findSortedBookings(String sortBy, String order) {
        if ("desc".equalsIgnoreCase(order)) {
            return tableBookingRepository.findAll(Sort.by(Sort.Direction.DESC, sortBy));
        } else {
            return tableBookingRepository.findAll(Sort.by(Sort.Direction.ASC, sortBy));
        }
    }
}
