package com.phamchien.mapper;

import com.phamchien.dto.group.GroupDto;
import com.phamchien.model.Group;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface GroupMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "permissions", target = "permissions")
    @Named("fromEntityToGroupDto")
    GroupDto fromEntityToGroupDto(Group group);
}
