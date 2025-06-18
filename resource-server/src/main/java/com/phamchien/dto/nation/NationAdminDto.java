package com.phamchien.dto.nation;

import com.phamchien.dto.ABasicAdminDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class NationAdminDto extends ABasicAdminDto {
    @ApiModelProperty(name = "name")
    private String name;
    @ApiModelProperty(name = "postCode")
    private String postCode;
    @ApiModelProperty(name = "kind")
    private Integer kind;
    @ApiModelProperty(name = "parent")
    private NationDto parent;
}
