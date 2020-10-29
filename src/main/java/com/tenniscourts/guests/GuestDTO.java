package com.tenniscourts.guests;

import com.tenniscourts.guests.api.GuestDTOApi;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GuestDTO implements GuestDTOApi {

    private Long id;

    @NotNull
    private String name;

}
