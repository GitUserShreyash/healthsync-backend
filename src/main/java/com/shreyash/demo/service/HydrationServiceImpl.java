package com.shreyash.demo.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shreyash.demo.dto.HydrationHistoryResponse;
import com.shreyash.demo.dto.HydrationLogRequest;
import com.shreyash.demo.dto.HydrationLogResponse;
import com.shreyash.demo.exception.ResourceNotFoundException;
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
	public List<HydrationHistoryResponse> getHistory() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userRepo.findByUsername(username).orElseThrow(()-> new RuntimeException("User Not Found"));
		LocalDate endDate = LocalDate.now();
	    LocalDate startDate = endDate.minusDays(14);
	    
	    List<HydrationLog> logs = hydrationLogRepo.findByUserAndLoggedAtBetweenOrderByLoggedAtDesc(user, startDate.atStartOfDay(), endDate.atTime(LocalTime.MAX));
	    return logs.stream()
	            .collect(Collectors.groupingBy(
	                    log -> log.getLoggedAt().toLocalDate(),
	                    Collectors.summingInt(HydrationLog::getAmountMl)
	            ))
	            .entrySet()
	            .stream()
	            .sorted(Map.Entry.<LocalDate, Integer>comparingByKey().reversed())
	            .map(entry -> new HydrationHistoryResponse(
	                    entry.getKey(),
	                    entry.getValue()
	            ))
	            .toList();
	}

	@Override
	@Transactional
	public String deleteHydrationLog(Long logId) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userRepo.findByUsername(username).orElseThrow(()-> new ResourceNotFoundException("User Not Found"));
		hydrationLogRepo.findById(logId).orElseThrow(() -> new ResourceNotFoundException("Hydration Log Not Found"));
		
		hydrationLogRepo.deleteByIdAndUser(logId, user);
		return "Hydration Log Deleted Successfully";
	}

	@Override
	public List<HydrationLogResponse> getTodaysLog() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userRepo.findByUsername(username).orElseThrow(()-> new ResourceNotFoundException("User Not Found"));
		
		return hydrationLogRepo.findByUserAndLoggedAtBetweenOrderByLoggedAtDesc(user, LocalDate.now().atStartOfDay(), LocalDateTime.now())
						.stream().map(mapper :: toDto).toList();
	}
	
	

}
