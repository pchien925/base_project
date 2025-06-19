package com.phamchien.mapper;

import com.phamchien.dto.product.ProductAdminDto;
import com.phamchien.dto.product.ProductAutoCompleteDto;
import com.phamchien.dto.settings.SettingsDto;
import com.phamchien.form.product.CreateProductForm;
import com.phamchien.form.product.UpdateProductForm;
import com.phamchien.model.Product;
import com.phamchien.model.Settings;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductMapper {
    ProductAutoCompleteDto fromEntityToProductAutoComplete(Product product);
    ProductAdminDto fromEntityToProductAdminDto(Product product);

    Product fromCreateProductFormToEntity(CreateProductForm createProductForm);

    void fromUpdateProductFormToEntity(UpdateProductForm updateProductForm,@MappingTarget Product product);

    @IterableMapping(elementTargetType = ProductAdminDto.class, qualifiedByName = "fromEntityToProductAdminDto")
    List<ProductAdminDto> fromEntityListToProductAdminDtoList(List<Product> products);
}
