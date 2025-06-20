package com.phamchien.dto.address;

import com.phamchien.dto.ABasicAdminDto;
import com.phamchien.dto.nation.NationAdminDto;
import com.phamchien.dto.user.UserDto;
import lombok.Data;

@Data
public class AddressAdminDto extends ABasicAdminDto {
    private String address;
    private NationAdminDto wardInfo;
    private NationAdminDto districtInfo;
    private NationAdminDto provinceInfo;
    private String name;
    private String phone;
    private UserDto userInfo;
}
