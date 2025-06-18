package com.phamchien.controller;

import com.phamchien.dto.ApiMessageDto;
import com.phamchien.dto.ErrorCode;
import com.phamchien.dto.ResponseListDto;
import com.phamchien.dto.user.UserAutoCompleteDto;
import com.phamchien.dto.user.UserDto;
import com.phamchien.form.user.UpdateUserForm;
import com.phamchien.mapper.UserMapper;
import com.phamchien.model.User;
import com.phamchien.model.criteria.UserCriteria;
import com.phamchien.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/user")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired(required = false)
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AddressRepository addressRepository;


    @GetMapping(value = "/get/{id}", produces= MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('US_V')")
    public ApiMessageDto<UserDto> getUser(@PathVariable("id") Long id)
    {
        ApiMessageDto<UserDto> apiMessageDto = new ApiMessageDto<>();
        User user = userRepository.findById(id).orElse(null);
        if (user==null)
        {
            apiMessageDto.setResult(false);
            apiMessageDto.setMessage("Not found user");
            apiMessageDto.setCode(ErrorCode.USER_ERROR_NOT_FOUND);
            return apiMessageDto;
        }
        apiMessageDto.setData(userMapper.fromEntityToUserDto(user));
        apiMessageDto.setMessage("get user success");
        return apiMessageDto;
    }

    @DeleteMapping(value = "/delete/{id}")
    @PreAuthorize("hasRole('US_D')")
    public ApiMessageDto<String> deleteUser(@PathVariable("id") Long id)
    {
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        User user = userRepository.findById(id).orElse(null);
        if (user==null)
        {
            apiMessageDto.setResult(false);
            apiMessageDto.setMessage("Not found user");
            apiMessageDto.setCode(ErrorCode.USER_ERROR_NOT_FOUND);
            return apiMessageDto;
        }

        addressRepository.deleteAllByUserId(id);
        userRepository.delete(user);

        apiMessageDto.setMessage("Delete User success");
        return apiMessageDto;
    }

    @GetMapping(value = "/list", produces= MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('US_L')")
    public ApiMessageDto<ResponseListDto<List<UserDto>>> getList(UserCriteria userCriteria , Pageable pageable)
    {

        ApiMessageDto<ResponseListDto<List<UserDto>>> apiMessageDto = new ApiMessageDto<>();
        ResponseListDto<List<UserDto>> responseListDto = new ResponseListDto<>();
        Page<User> listUser = userRepository.findAll(userCriteria.getSpecification(),pageable);
        responseListDto.setContent(userMapper.fromUserListToUserDtoList(listUser.getContent()));
        responseListDto.setTotalPages(listUser.getTotalPages());
        responseListDto.setTotalElements(listUser.getTotalElements());

        apiMessageDto.setData(responseListDto);
        apiMessageDto.setMessage("Get list user success");
        return apiMessageDto;
    }

    @GetMapping(value = "/auto-complete",produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListDto<List<UserAutoCompleteDto>>> ListAutoComplete(UserCriteria userCriteria)
    {
        ApiMessageDto<ResponseListDto<List<UserAutoCompleteDto>>> apiMessageDto = new ApiMessageDto<>();
        ResponseListDto<List<UserAutoCompleteDto>> responseListDto = new ResponseListDto<>();
        Pageable pageable = PageRequest.of(0,10);
        Page<User> listUser =userRepository.findAll(userCriteria.getSpecification(),pageable);
        responseListDto.setContent(userMapper.fromUserListToUserDtoListAutocomplete(listUser.getContent()));
        responseListDto.setTotalPages(listUser.getTotalPages());
        responseListDto.setTotalElements(listUser.getTotalElements());

        apiMessageDto.setData(responseListDto);
        apiMessageDto.setMessage("get success");
        return apiMessageDto;
    }
    @Transactional
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('US_U')")
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateUserForm updateUserForm, BindingResult bindingResult) {

        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        User user = userRepository.findById(updateUserForm.getId()).orElse(null);
        if (user==null)
        {
            apiMessageDto.setResult(false);
            apiMessageDto.setMessage("Not found user");
            apiMessageDto.setCode(ErrorCode.USER_ERROR_NOT_FOUND);
            return apiMessageDto;
        }

        apiMessageDto.setMessage("update success");
        return apiMessageDto;
    }





}
