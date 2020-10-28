package com.tenniscourts.reservations.api;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "CreateReservationRequestDTO")
public interface CreateReservationRequestDTOApi {

    @ApiModelProperty(required = true, value = "Guest Id")
    Long getGuestId();

    @ApiModelProperty(required = true, value = "Schedule Id")
    Long getScheduleId();

}
