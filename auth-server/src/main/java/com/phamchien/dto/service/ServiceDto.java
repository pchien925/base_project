package com.phamchien.dto.service;

import com.phamchien.dto.ABasicAdminDto;
import com.phamchien.dto.account.AccountDto;
import com.phamchien.model.Group;
import lombok.Data;

@Data
public class ServiceDto extends ABasicAdminDto {
    private String serviceName;
    private Group group;
    private String logoPath;
    private String bannerPath;
    private String hotline;
    private String settings;
    private String lang;
    private Integer status;
    private AccountDto accountDto;

    private String tenantId;
}
