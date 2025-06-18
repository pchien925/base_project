package com.phamchien.dto.user;

import com.phamchien.dto.account.AccountDto;
import lombok.Data;

import java.util.Date;

@Data
public class UserDto {

    private Long id;
    private Date birthday;
    private AccountDto account;

}
