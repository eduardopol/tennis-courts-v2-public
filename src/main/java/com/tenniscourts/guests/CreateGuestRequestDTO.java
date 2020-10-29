package com.tenniscourts.guests;

import com.tenniscourts.guests.api.CreateGuestRequestDTOApi;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CreateGuestRequestDTO implements CreateGuestRequestDTOApi {

    @NotNull
    private String name;

}
