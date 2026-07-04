package com.shreyash.demo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.shreyash.demo.dto.ProfileRequest;
import com.shreyash.demo.dto.ProfileResponse;
import com.shreyash.demo.model.UserProfile;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
	ProfileResponse toDto(UserProfile profile);
	UserProfile toEntity(ProfileRequest req);
	void updateEntity(ProfileRequest dto, @MappingTarget UserProfile entity);
}
