package com.phamchien.dto.news;

import com.phamchien.dto.ABasicAdminDto;
import com.phamchien.dto.category.CategoryDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class NewsAdminDto extends ABasicAdminDto {

    @ApiModelProperty(name = "title")
    private String title;

    @ApiModelProperty(name = "content")
    private String content;

    @ApiModelProperty(name = "avatar")
    private String avatar;

    @ApiModelProperty(name = "banner")
    private String banner;

    @ApiModelProperty(name = "description")
    private String description;

    @ApiModelProperty(name = "category")
    private CategoryDto category;

    @ApiModelProperty(name = "pinTop")
    private Integer pinTop;
}
