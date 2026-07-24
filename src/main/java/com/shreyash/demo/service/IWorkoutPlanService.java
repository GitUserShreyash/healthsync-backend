package com.shreyash.demo.service;

import java.util.List;

import com.shreyash.demo.dto.WorkoutPlanResponse;
import com.shreyash.demo.model.User;
import com.shreyash.demo.model.UserProfile;

public interface IWorkoutPlanService {
	//void generatePlan(User user,UserProfile profile);

    List<WorkoutPlanResponse> getWeeklyPlan();

    //String completeWorkout(Long planId);

    WorkoutPlanResponse getTodayWorkout();
}
