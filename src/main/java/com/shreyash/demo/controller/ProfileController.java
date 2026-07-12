package com.shreyash.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shreyash.demo.dto.ProfileRequest;
import com.shreyash.demo.dto.ProfileResponse;
import com.shreyash.demo.service.IProfileService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class ProfileController {
	@Autowired
	private IProfileService profileService;
	
	@PutMapping("/profile")
	public ResponseEntity<ProfileResponse> updateProfile(@Valid @RequestBody ProfileRequest req){
		return ResponseEntity.ok(profileService.updateProfile(req));
	}
	
	@GetMapping("/profile")
	public ResponseEntity<ProfileResponse> getProfile(@Valid @RequestBody ProfileRequest req){
		return ResponseEntity.ok(profileService.getProfile());
	}
}
