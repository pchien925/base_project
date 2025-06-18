package com.phamchien.mapper;

import com.phamchien.dto.user.UserAutoCompleteDto;
import com.phamchien.dto.user.UserDto;
import com.phamchien.model.User;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "id",target = "id")
    @Mapping(source = "birthday",target = "birthday", dateFormat = "yyyy-MM-dd")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromUserToUserDto")
    UserDto fromEntityToUserDto(User user);

    @Mapping(source = "id",target = "id")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromUserToUserDtoAutoComplete")
    UserAutoCompleteDto fromUserToDtoAutoComplete(User user);

    @IterableMapping(elementTargetType = UserDto.class,qualifiedByName = "fromUserToUserDto")
    @BeanMapping(ignoreByDefault = true)
    List<UserDto> fromUserListToUserDtoList(List<User> list);

    @IterableMapping(elementTargetType = UserAutoCompleteDto.class,qualifiedByName = "fromUserToUserDtoAutoComplete")
    @BeanMapping(ignoreByDefault = true)
    List<UserAutoCompleteDto> fromUserListToUserDtoListAutocomplete(List<User> list);
}
