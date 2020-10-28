package com.tenniscourts.reservations;

import com.tenniscourts.config.BaseRestController;
import com.tenniscourts.reservations.api.ReservationControllerApi;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@RestController
@RequestMapping(value = ReservationController.URL_BASE)
public class ReservationController extends BaseRestController implements ReservationControllerApi {

    public static final String URL_BASE = "/tennis-courts/reservations";

    private final ReservationService reservationService;

    @Override
    @PostMapping
    public ResponseEntity<Void> bookReservation(@Valid @RequestBody CreateReservationRequestDTO createReservationRequestDTO) {
        return ResponseEntity.created(locationByEntity(reservationService.bookReservation(createReservationRequestDTO).getId())).build();
    }

    @Override
    @GetMapping("/{reservationId}")
    public ResponseEntity<ReservationDTO> findReservation(@PathVariable @NotNull Long reservationId) {
        return ResponseEntity.ok(reservationService.findReservation(reservationId));
    }

    @Override
    @PostMapping("/{reservationId}/cancel")
    public ResponseEntity<ReservationDTO> cancelReservation(@PathVariable @NotNull Long reservationId) {
        return ResponseEntity.ok(reservationService.cancelReservation(reservationId));
    }

    @Override
    @PostMapping("/{reservationId}/finish")
    public ResponseEntity<ReservationDTO> finishReservation(@PathVariable @NotNull Long reservationId) {
        return ResponseEntity.ok(reservationService.finishReservation(reservationId));
    }

    @Override
    @PutMapping("/{reservationId}/reschedule/{scheduleId}")
    public ResponseEntity<ReservationDTO> rescheduleReservation(@PathVariable @NotNull Long reservationId,
                                                                @PathVariable @NotNull Long scheduleId) {
        return ResponseEntity.ok(reservationService.rescheduleReservation(reservationId, scheduleId));
    }
}
