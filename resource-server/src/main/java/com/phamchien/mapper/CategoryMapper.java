package com.phamchien.mapper;

import com.phamchien.dto.category.CategoryDto;
import com.phamchien.form.category.CreateCategoryForm;
import com.phamchien.form.category.UpdateCategoryForm;
import com.phamchien.model.Category;
import org.mapstruct.*;

import java.util.List;


@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "image", target = "image")
    @Mapping(source = "ordering", target = "ordering")
    @Mapping(source = "kind", target = "kind")
    @Named("fromCreateCategory")
    @BeanMapping(ignoreByDefault = true)
    Category fromCreateCategory(CreateCategoryForm createCategoryForm);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "image", target = "image")
    @Mapping(source = "ordering", target = "ordering")
    @Named("mappingForUpdateCategory")
    @BeanMapping(ignoreByDefault = true)
    void mappingForUpdateServiceCategory(UpdateCategoryForm updateServiceCategoryForm, @MappingTarget Category category);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "image", target = "image")
    @Mapping(source = "ordering", target = "ordering")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "modifiedDate", target = "modifiedDate")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "status", target = "status")
    @Named("fromEntityToCategoryDto")
    @BeanMapping(ignoreByDefault = true)
    CategoryDto fromEntityToCategoryDto(Category category);
    @IterableMapping(elementTargetType = CategoryDto.class, qualifiedByName = "fromEntityToCategoryDto")
    List<CategoryDto> fromEntityToDtoList(List<Category> categories);


    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "image", target = "image")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromCategoryToCompleteDto")
    CategoryDto fromCategoryToCompleteDto(Category category);

    @IterableMapping(elementTargetType = CategoryDto.class, qualifiedByName = "fromCategoryToCompleteDto")
    List<CategoryDto> fromCategoryToComplteDtoList(List<Category> categories);
}
