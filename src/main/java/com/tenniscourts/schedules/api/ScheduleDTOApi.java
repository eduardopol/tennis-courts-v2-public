package com.tenniscourts.schedules.api;

import com.tenniscourts.tenniscourts.TennisCourtDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;

@ApiModel(value = "ScheduleDTO", description = "Schedule details")
public interface ScheduleDTOApi {

    @ApiModelProperty(value = "Schedule Id")
    Long getId();

    @ApiModelProperty(value = "Tennis Court Information")
    TennisCourtDTO getTennisCourt();

    @ApiModelProperty(value = "Tennis Court Id", required = true)
    Long getTennisCourtId();

    @ApiModelProperty(required = true, value = "Start date and time of the schedule", example = "2020-12-02T08:33")
    LocalDateTime getStartDateTime();

    @ApiModelProperty(required = true, value = "End date and time of the schedule", example = "2020-12-02T08:33")
    LocalDateTime getEndDateTime();


}
