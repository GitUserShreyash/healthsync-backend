package com.shreyash.demo.service;

import java.util.List;

import com.shreyash.demo.dto.BodyMetricRequest;
import com.shreyash.demo.dto.BodyMetricResponse;
import com.shreyash.demo.model.BodyMetrics;
import com.shreyash.demo.model.User;
import com.shreyash.demo.model.UserProfile;

public interface IBodyMetricsService {
	void logMetric(UserProfile profile, User user);

	List<BodyMetricResponse> getHistory();
}
