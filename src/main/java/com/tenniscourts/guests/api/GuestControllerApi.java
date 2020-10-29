package com.tenniscourts.guests.api;


import com.tenniscourts.guests.CreateGuestRequestDTO;
import com.tenniscourts.guests.GuestDTO;
import com.tenniscourts.guests.UpdateGuestRequestDTO;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ApiResponses(value = {
        @ApiResponse(code = 400, message = "Invalid parameter"),
        @ApiResponse(code = 404, message = "Resource not found"),
        @ApiResponse(code = 412, message = "Precondition failed"),
        @ApiResponse(code = 500, message = "Internal Server Error"),
        @ApiResponse(code = 503, message = "Communication Error") })
@Api(tags = "API - Guests")
public interface GuestControllerApi {

    @ApiOperation(value = "Create a new Guest", notes = "Endpoint responsible for creating a new Guest.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Guest created successfully.") })
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<Void> addGuest(
            @ApiParam(value = "Guest to be created", required = true) @RequestBody CreateGuestRequestDTO createGuestRequestDTO);

    @ApiOperation(value = "Update a Guest", notes = "Endpoint responsible for updating a Guest.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Guest updated successfully.") })
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<Void> updateGuest(
            @ApiParam(value = "Guest ID", required = true) @PathVariable("guestId") Long guestId,
            @ApiParam(value = "Guest to be updated", required = true) @RequestBody UpdateGuestRequestDTO updateGuestRequestDTO);

    @ApiOperation(value = "Delete a Guest", notes = "Endpoint responsible for deleting a Guest.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Guest deleted successfully.") })
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<Void> deleteGuest(
            @ApiParam(value = "Guest ID", required = true) @PathVariable("guestId") Long guestId);

    @ApiOperation(value = "Find an existing Guest", notes = "Endpoint responsible for finding an existing Guest.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = GuestDTO.class) })
    ResponseEntity<GuestDTO> findGuest(
            @ApiParam(value = "Guest ID", required = true) @PathVariable("GuestId") Long GuestId);

    @ApiOperation(value = "Find an existing Guest by name", notes = "Endpoint responsible for finding an existing Guest by name.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = GuestDTO.class) })
    ResponseEntity<GuestDTO> findGuestByName(
            @ApiParam(value = "Guest name", example = "Roger Federer")  @RequestParam(name = "guestName", required = true)  String guestName);

    @ApiOperation(value = "Find all existing Guests", notes = "Endpoint responsible for finding all existing Guests.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = GuestDTO.class, responseContainer = "List")})
    ResponseEntity<List<GuestDTO>> findAllGuests();

}
