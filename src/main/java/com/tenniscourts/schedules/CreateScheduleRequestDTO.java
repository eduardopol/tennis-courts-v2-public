package com.tenniscourts.schedules;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tenniscourts.schedules.api.CreateScheduleRequestDTOApi;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
public class CreateScheduleRequestDTO implements CreateScheduleRequestDTOApi {

    @NotNull
    private Long tennisCourtId;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @NotNull
    private LocalDateTime startDateTime;

}
