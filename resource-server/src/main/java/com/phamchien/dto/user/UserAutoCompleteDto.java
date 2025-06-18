package com.phamchien.dto.user;

import com.phamchien.dto.account.AccountAutoCompleteDto;
import lombok.Data;

@Data
public class UserAutoCompleteDto {

    private Long id;
    private AccountAutoCompleteDto accountAutoCompleteDto;
}
