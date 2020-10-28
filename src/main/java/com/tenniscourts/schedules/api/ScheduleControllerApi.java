package com.tenniscourts.schedules.api;

import com.tenniscourts.schedules.CreateScheduleRequestDTO;
import com.tenniscourts.schedules.ScheduleDTO;
import io.swagger.annotations.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDate;
import java.util.List;

@ApiResponses(value = {
        @ApiResponse(code = 400, message = "Invalid parameter"),
        @ApiResponse(code = 404, message = "Resource not found"),
        @ApiResponse(code = 412, message = "Precondition failed"),
        @ApiResponse(code = 500, message = "Internal Server Error"),
        @ApiResponse(code = 503, message = "Communication Error") })
@Api(tags = "API - Schedule")
public interface ScheduleControllerApi {

    @ApiOperation(value = "Create a new schedule", notes = "Endpoint responsible for creating a new schedule.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Schedule created successfully.") })
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<Void> addScheduleTennisCourt(
            @ApiParam(value = "Schedule to be created", required = true) @RequestBody CreateScheduleRequestDTO createScheduleRequestDTO);

    @ApiOperation(value = "Find all existing schedules between the dates in the search", notes = "Endpoint responsible for finding all existing schedules between the dates.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ScheduleDTO.class, responseContainer = "List")})
    ResponseEntity<List<ScheduleDTO>>  findSchedulesByDates(
            @RequestParam(name = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(name = "endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate);

    @ApiOperation(value = "Find an existing schedule", notes = "Endpoint responsible for finding an existing schedule.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ScheduleDTO.class) })
    ResponseEntity<ScheduleDTO> findByScheduleId(
            @ApiParam(value = "Schedule ID", required = true) @PathVariable("scheduleId") Long scheduleId);

}
