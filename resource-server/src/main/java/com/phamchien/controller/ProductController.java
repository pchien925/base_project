package com.phamchien.controller;

import com.phamchien.dto.ApiMessageDto;
import com.phamchien.dto.ErrorCode;
import com.phamchien.dto.ResponseListDto;
import com.phamchien.dto.product.ProductAdminDto;
import com.phamchien.form.product.CreateProductForm;
import com.phamchien.form.product.UpdateProductForm;
import com.phamchien.mapper.ProductMapper;
import com.phamchien.model.Product;
import com.phamchien.model.criteria.ProductCriteria;
import com.phamchien.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/v1/product")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProductController extends ABasicController{
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    @PostMapping(value = "/create", produces= MediaType.APPLICATION_JSON_VALUE)
    //@PreAuthorize("hasRole('PRODUCT_C')")
    public ApiMessageDto<String> create(@Valid @RequestBody CreateProductForm createProductForm, BindingResult bindingResult) {
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Product product = productMapper.fromCreateProductFormToEntity(createProductForm);
        product.setReusedId("SPRINGBOOTLASOTG2FRAMEWORK");
        productRepository.save(product);
        apiMessageDto.setMessage("Create product success");
        return apiMessageDto;
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    //@PreAuthorize("hasRole('PRODUCT_U')")
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateProductForm updateProductForm, BindingResult bindingResult) {
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Product product = productRepository.findById(updateProductForm.getId()).orElse(null);
        if(product == null){
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.SETTINGS_ERROR_NOT_FOUND);
            apiMessageDto.setMessage("Not found product");
            return apiMessageDto;
        }
        productMapper.fromUpdateProductFormToEntity(updateProductForm, product);
        productRepository.save(product);
        apiMessageDto.setMessage("Update product success");
        return apiMessageDto;
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    //@PreAuthorize("hasRole('PRODUCT_D')")
    public ApiMessageDto<String> delete(@PathVariable("id") String id) {
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();

        Product product = productRepository.findById(id).orElse(null);
        if(product == null){
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.SETTINGS_ERROR_NOT_FOUND);
            apiMessageDto.setMessage("Not found product");
            return apiMessageDto;
        }
        productRepository.deleteById(id);
        apiMessageDto.setMessage("Delete product success");
        return apiMessageDto;
    }

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    //@PreAuthorize("hasRole('PRODUCT_L')")
    public ApiMessageDto<ResponseListDto<List<ProductAdminDto>>> list(ProductCriteria productCriteria, Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();
        Map<String, Object> claims = (Map<String, Object>) details.getDecodedDetails();
        String username = (String) claims.get("user_name");

        Long currentUserId = getCurrentUser();

        ApiMessageDto<ResponseListDto<List<ProductAdminDto>>> apiMessageDto = new ApiMessageDto<>();
        Page<Product> products = productRepository.findAll(productCriteria.getSpecification(), pageable);
        ResponseListDto<List<ProductAdminDto>> responseListDto = new ResponseListDto();
        responseListDto.setContent(productMapper.fromEntityListToProductAdminDtoList(products.getContent()));
        responseListDto.setTotalElements(products.getTotalElements());
        responseListDto.setTotalPages(products.getTotalPages());

        apiMessageDto.setData(responseListDto);
        apiMessageDto.setMessage("Get list products success");
        return apiMessageDto;
    }
}

