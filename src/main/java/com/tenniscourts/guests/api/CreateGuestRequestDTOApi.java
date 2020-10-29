package com.tenniscourts.guests.api;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "CreateGuestRequestDTO")
public interface CreateGuestRequestDTOApi {

    @ApiModelProperty(value = "Guest name", dataType="java.lang.String", allowableValues="range[-infinity, 4000]")
    String getName();

}
