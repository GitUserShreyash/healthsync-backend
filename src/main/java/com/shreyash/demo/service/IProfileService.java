package com.shreyash.demo.service;

import com.shreyash.demo.dto.ProfileRequest;
import com.shreyash.demo.dto.ProfileResponse;

public interface IProfileService {
	ProfileResponse getProfile();
	ProfileResponse updateProfile(ProfileRequest req);
}
