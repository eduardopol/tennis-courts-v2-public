package com.tenniscourts.schedules.api;

import com.tenniscourts.reservations.ReservationDTO;
import com.tenniscourts.schedules.ScheduleDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@ApiModel(value = "CreateScheduleRequestDTOApi", description = "Create Schedule Request")
public interface CreateScheduleRequestDTOApi {

    @ApiModelProperty(value = "Tennis Court Id", required = true)
    Long getTennisCourtId();

    @ApiModelProperty(required = true, value = "Start date and time of the schedule", example = "2020-12-02T08:33")
    LocalDateTime getStartDateTime();

}
