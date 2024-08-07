package com.example.capstone_project.utils.mapper.user.edit;


import com.example.capstone_project.controller.body.user.update.UpdateUserBody;
import com.example.capstone_project.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UpdateUserBodyToUserEntityMapper {
    @Mapping(source = "departmentId", target = "department.id")
    @Mapping(source = "roleId", target = "role.id")
    @Mapping(source = "positionId", target = "position.id")
    User updateUserFromDto(UpdateUserBody dto);
}
