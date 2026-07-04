package com.shreyash.demo.service;

import java.util.List;

import com.shreyash.demo.dto.HydrationLogRequest;
import com.shreyash.demo.dto.HydrationLogResponse;

public interface IHydrationService {
	HydrationLogResponse logWater(HydrationLogRequest req);
	List<HydrationLogResponse> getHistory();
}
