package com.phamchien.dto.account;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ForgetPasswordDto {
    @ApiModelProperty(name = "idHash")
    private String idHash;
}
