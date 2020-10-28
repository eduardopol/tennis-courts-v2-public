package com.tenniscourts.reservations;

import com.tenniscourts.reservations.api.CreateReservationRequestDTOApi;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Data
public class CreateReservationRequestDTO implements CreateReservationRequestDTOApi {

    @NotNull
    private Long guestId;

    @NotNull
    private Long scheduleId;

}
