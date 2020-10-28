package com.tenniscourts.reservations.api;


import com.tenniscourts.reservations.CreateReservationRequestDTO;
import com.tenniscourts.reservations.ReservationDTO;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ApiResponses(value = {
        @ApiResponse(code = 400, message = "Invalid parameter"),
        @ApiResponse(code = 404, message = "Resource not found"),
        @ApiResponse(code = 412, message = "Precondition failed"),
        @ApiResponse(code = 500, message = "Internal Server Error"),
        @ApiResponse(code = 503, message = "Communication Error") })
@Api(tags = "API - Reservations")
public interface ReservationControllerApi {

    @ApiOperation(value = "Create a new reservation", notes = "Endpoint responsible for creating a new reservation.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Reservation created successfully.") })
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<Void> bookReservation(
            @ApiParam(value = "Reservation to be created", required = true) @RequestBody CreateReservationRequestDTO createReservationRequestDTO);

    @ApiOperation(value = "Find an existing reservation", notes = "Endpoint responsible for finding an existing reservation.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ReservationDTO.class) })
    ResponseEntity<ReservationDTO> findReservation(
            @ApiParam(value = "Reservation ID", required = true) @PathVariable("reservationId") Long reservationId);

    @ApiOperation(value = "Cancel an existing reservation", notes = "Endpoint responsible for canceling an existing reservation.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Reservation successfully canceled", response = ReservationDTO.class) })
    ResponseEntity<ReservationDTO> cancelReservation(
            @ApiParam(value = "Reservation ID", required = true) @PathVariable("reservationId") Long reservationId);

    @ApiOperation(value = "Finish an existing reservation", notes = "Endpoint responsible for finishing an existing reservation.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Reservation successfully finished", response = ReservationDTO.class) })
    ResponseEntity<ReservationDTO> finishReservation(
            @ApiParam(value = "Reservation ID", required = true) @PathVariable("reservationId") Long reservationId);

    @ApiOperation(value = "Reschedule an existing reservation", notes = "Endpoint responsible for rescheduling an existing reservation.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Reservation rescheduled successfully", response = ReservationDTO.class) })
    ResponseEntity<ReservationDTO> rescheduleReservation(
            @ApiParam(value = "Reservation ID", required = true) @PathVariable("reservationId") Long reservationId,
            @ApiParam(value = "Schedule ID", required = true) @PathVariable("scheduleId") Long scheduleId);
}
