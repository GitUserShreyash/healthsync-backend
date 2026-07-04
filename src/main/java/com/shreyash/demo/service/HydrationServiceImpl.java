package com.shreyash.demo.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.shreyash.demo.dto.HydrationLogRequest;
import com.shreyash.demo.dto.HydrationLogResponse;
import com.shreyash.demo.mapper.HydrationLogMapper;
import com.shreyash.demo.model.HydrationLog;
import com.shreyash.demo.model.User;
import com.shreyash.demo.repo.HydrationLogRepository;
import com.shreyash.demo.repo.UserRepository;

@Service
public class HydrationServiceImpl implements IHydrationService {

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private HydrationLogMapper mapper;
	
	@Autowired
	private HydrationLogRepository hydrationLogRepo;
	
	@Override
	public HydrationLogResponse logWater(HydrationLogRequest req) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userRepo.findByUsername(username).orElseThrow(()-> new RuntimeException("User Not Found"));
		
		HydrationLog log = mapper.toEntity(req);
		log.setUser(user);
		log.setLoggedAt(LocalDateTime.now());
		
		return mapper.toDto(hydrationLogRepo.save(log));
	}

	@Override
	public List<HydrationLogResponse> getHistory() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userRepo.findByUsername(username).orElseThrow(()-> new RuntimeException("User Not Found"));
		
		return hydrationLogRepo.findByUserOrderByLoggedAtDesc(user).stream().map(mapper::toDto).toList();
	}

}
