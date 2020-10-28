package com.tenniscourts.tenniscourts.api;

import com.tenniscourts.schedules.ScheduleDTO;
import com.tenniscourts.tenniscourts.TennisCourtDTO;
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
@Api(tags = "API - Tennis Court")
public interface TennisCourtControllerApi {

    @ApiOperation(value = "Create a new Tennis Court", notes = "Endpoint responsible for creating a new Tennis Court.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Tennis Court created successfully.") })
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<Void> addTennisCourt(
            @ApiParam(value = "Tennis Court to be created", required = true) @RequestBody TennisCourtDTO tennisCourtDTO);

    @ApiOperation(value = "Find an existing Tennis Court", notes = "Endpoint responsible for finding an existing Tennis Court.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = TennisCourtDTO.class) })
    ResponseEntity<TennisCourtDTO> findTennisCourtById(
            @ApiParam(value = "Tennis Court ID", required = true) @PathVariable("tennisCourtId") Long tennisCourtId);

    @ApiOperation(value = "Find an existing Tennis Court with the schedule", notes = "Endpoint responsible for finding an existing Tennis Court with the schedule.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = TennisCourtDTO.class) })
    ResponseEntity<TennisCourtDTO> findTennisCourtWithSchedulesById(
            @ApiParam(value = "Tennis Court ID", required = true) @PathVariable("tennisCourtId") Long tennisCourtId);
}
