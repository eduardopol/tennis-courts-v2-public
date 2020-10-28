package com.tenniscourts.tenniscourts.api;

import com.tenniscourts.reservations.ReservationDTO;
import com.tenniscourts.schedules.ScheduleDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.List;

@ApiModel(value = "TennisCourtDTO", description = "Tennis Court details")
public interface TennisCourtDTOApi {

    @ApiModelProperty(value = "Tennis Court Id")
    Long getId();

    @ApiModelProperty(value = "Tennis Court name", dataType="java.lang.String", allowableValues="range[-infinity, 4000]")
    String getName();

    @ApiModelProperty(value = "Schedule Information for the Tennis Court")
    List<ScheduleDTO> getTennisCourtSchedules();

}
