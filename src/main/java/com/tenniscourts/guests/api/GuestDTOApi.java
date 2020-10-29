package com.tenniscourts.guests.api;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "GuestDTOApi", description = "Guest details")
public interface GuestDTOApi {

    @ApiModelProperty(value = "Guest Id")
    Long getId();

    @ApiModelProperty(value = "Guest name", dataType="java.lang.String", allowableValues="range[-infinity, 4000]")
    String getName();


}
