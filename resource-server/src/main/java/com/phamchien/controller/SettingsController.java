package com.phamchien.controller;

import com.phamchien.constant.UserBaseConstant;
import com.phamchien.dto.ApiMessageDto;
import com.phamchien.dto.ErrorCode;
import com.phamchien.dto.ResponseListDto;
import com.phamchien.dto.settings.SettingsAutoCompleteDto;
import com.phamchien.dto.settings.SettingsDto;
import com.phamchien.form.settings.CreateSettingsForm;
import com.phamchien.form.settings.UpdateSettingsForm;
import com.phamchien.mapper.SettingsMapper;
import com.phamchien.model.Settings;
import com.phamchien.model.criteria.SettingsCriteria;
import com.phamchien.repository.SettingsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/settings")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class SettingsController extends ABasicController{
    @Autowired
    SettingsRepository settingsRepository;

    @Autowired(required = false)
    SettingsMapper settingsMapper;

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('SET_V')")
    public ApiMessageDto<SettingsDto> get(@PathVariable("id") Long id) {
        ApiMessageDto<SettingsDto> apiMessageDto = new ApiMessageDto<>();
        Settings settings = settingsRepository.findById(id).orElse(null);
        if(settings == null){
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.SETTINGS_ERROR_NOT_FOUND);
            apiMessageDto.setMessage("Not found setting");
            return apiMessageDto;
        }
        apiMessageDto.setData(settingsMapper.fromEntityToSettingsDto(settings));
        apiMessageDto.setMessage("Get settings by id success");
        return apiMessageDto;
    }

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('SET_L')")
    public ApiMessageDto<ResponseListDto<List<SettingsDto>>> list(SettingsCriteria settingsCriteria, Pageable pageable) {
        ApiMessageDto<ResponseListDto<List<SettingsDto>>> apiMessageDto = new ApiMessageDto<>();
        Page<Settings> settings = settingsRepository.findAll(SettingsCriteria.findSettingsByCriteria(settingsCriteria), pageable);
        ResponseListDto<List<SettingsDto>> responseListDto = new ResponseListDto();
        responseListDto.setContent(settingsMapper.fromEntityListToSettingsDtoList(settings.getContent()));
        responseListDto.setTotalElements(settings.getTotalElements());
        responseListDto.setTotalPages(settings.getTotalPages());

        apiMessageDto.setData(responseListDto);
        apiMessageDto.setMessage("Get list settings success");
        return apiMessageDto;
    }

    @GetMapping(value = "/auto-complete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListDto<List<SettingsAutoCompleteDto>>> autoComplete(SettingsCriteria settingsCriteria) {
        ApiMessageDto<ResponseListDto<List<SettingsAutoCompleteDto>>> responseListDtoApiMessageDto = new ApiMessageDto<>();

        Pageable pageable = PageRequest.of(0,10);
        settingsCriteria.setStatus(UserBaseConstant.STATUS_ACTIVE);
        Page<Settings> settings = settingsRepository.findAll(SettingsCriteria.findSettingsByCriteria(settingsCriteria), pageable);
        ResponseListDto<List<SettingsAutoCompleteDto>> responseListObj = new ResponseListDto<>();
        responseListObj.setContent(settingsMapper.fromEntityListToSettingsAutoCompleteDtoList(settings.getContent()));
        responseListObj.setTotalPages(settings.getTotalPages());
        responseListObj.setTotalElements(settings.getTotalElements());

        responseListDtoApiMessageDto.setData(responseListObj);
        responseListDtoApiMessageDto.setMessage("Get auto complete setting success");
        return responseListDtoApiMessageDto;
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('SET_C')")
    public ApiMessageDto<String> create(@Valid @RequestBody CreateSettingsForm createSettingsForm, BindingResult bindingResult) {
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Settings exsitingSettings = settingsRepository.findBySettingKey(createSettingsForm.getSettingKey());
        if(exsitingSettings != null){
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.SETTINGS_ERROR_SETTING_KEY_EXISTED);
            apiMessageDto.setMessage("Setting key exist");
            return apiMessageDto;
        }
        Settings settings = settingsMapper.fromCreateSettingsFormToEntity(createSettingsForm);
        settingsRepository.save(settings);
        apiMessageDto.setMessage("Create setting success");
        return apiMessageDto;
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('SET_U')")
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateSettingsForm updateSettingsForm, BindingResult bindingResult) {
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Settings settings = settingsRepository.findById((updateSettingsForm.getId())).orElse(null);
        if(settings == null){
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.SETTINGS_ERROR_NOT_FOUND);
            apiMessageDto.setMessage("Not found setting");
            return apiMessageDto;
        }
        settingsMapper.fromUpdateSettingsFormToEntity(updateSettingsForm, settings);
        settingsRepository.save(settings);
        apiMessageDto.setMessage("Update settings success");
        return apiMessageDto;
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('SET_D')")
    public ApiMessageDto<String> delete(@PathVariable("id") Long id) {
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();

        Settings settings = settingsRepository.findById(id).orElse(null);
        if(settings == null){
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.SETTINGS_ERROR_NOT_FOUND);
            apiMessageDto.setMessage("Not found setting");
            return apiMessageDto;
        }
        settingsRepository.deleteById(id);
        apiMessageDto.setMessage("Delete settings success");
        return apiMessageDto;
    }
}
