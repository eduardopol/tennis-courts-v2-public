package com.tenniscourts.reservations.api;

import com.tenniscourts.reservations.ReservationDTO;
import com.tenniscourts.schedules.ScheduleDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

@ApiModel(value = "ReservationDTO", description = "Reservation details")
public interface ReservationDTOApi {

    @ApiModelProperty(value = "Reservation Id")
    Long getId();

    @ApiModelProperty(value = "Schedule Information")
    ScheduleDTO getSchedule();

    @ApiModelProperty(value = "Reservation Status", dataType="java.lang.String", allowableValues="range[-infinity, 4000]")
    String getReservationStatus();

    @ApiModelProperty(value = "Previous Reservation")
    ReservationDTO getPreviousReservation();

    @ApiModelProperty(value = "Refund Value")
    BigDecimal getRefundValue();

    @ApiModelProperty(value = "Reservation Value")
    BigDecimal getValue();

    @ApiModelProperty(value = "Guest Id", required = true)
    Long getGuestId();

    @ApiModelProperty(value = "Schedule Id", required = true)
    Long getScheduledId();
}
