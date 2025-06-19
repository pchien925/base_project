package com.phamchien.form.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@ApiModel
public class CreateProductForm {
    @NotEmpty(message = "name can not empty")
    private String name;

    @NotEmpty(message = "description can not empty")
    private String description;
}
