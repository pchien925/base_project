package com.phamchien.mapper;

import com.phamchien.dto.account.AccountAutoCompleteDto;
import com.phamchien.dto.account.AccountDto;
import com.phamchien.model.Account;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {GroupMapper.class})
public interface AccountMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "fullName", target = "fullName")
    @Mapping(source = "group", target = "group", qualifiedByName = "fromEntityToGroupDto")
    @Mapping(source = "lastLogin", target = "lastLogin")
    @Mapping(source = "avatarPath", target = "avatar")
    @Mapping(source = "isSuperAdmin", target = "isSuperAdmin")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromAccountToDto")
    AccountDto fromAccountToDto(Account account);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "avatarPath", target = "avatarPath")
    @Mapping(source = "fullName", target = "fullName")
    @Named("fromAccountToAutoCompleteDto")
    AccountAutoCompleteDto fromAccountToAutoCompleteDto(Account account);


    @IterableMapping(elementTargetType = AccountAutoCompleteDto.class)
    List<AccountAutoCompleteDto> convertAccountToAutoCompleteDto(List<Account> list);
}
