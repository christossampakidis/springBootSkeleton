package com.demoapp.demoapp.mappers;

import org.mapstruct.Mapper;

import com.demoapp.demoapp.entities.UserEntity;
import com.demoapp.demoapp.models.UserRequest;
import com.demoapp.demoapp.models.UserResponse;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toUserResponse(UserEntity entity);

    UserEntity toUserEntity(UserRequest request);
}
