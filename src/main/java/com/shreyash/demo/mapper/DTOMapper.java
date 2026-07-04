package com.shreyash.demo.mapper;

import org.springframework.stereotype.Component;

import com.shreyash.demo.dto.UserResponse;
import com.shreyash.demo.model.User;
import com.shreyash.demo.model.UserProfile;

@Component
public class DTOMapper {
	public UserResponse mapToDTO(User user) {
		UserResponse userResp = new UserResponse();
		userResp.setId(user.getId());
		userResp.setEmail(user.getEmail());
		userResp.setUsername(user.getUsername());
		return userResp;
	}
	
	
}
