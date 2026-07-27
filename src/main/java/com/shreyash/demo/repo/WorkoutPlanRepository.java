package com.shreyash.demo.repo;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shreyash.demo.enums.GoalType;
import com.shreyash.demo.model.WorkoutPlan;

@Repository
public interface WorkoutPlanRepository extends JpaRepository<WorkoutPlan, Long> {
    
    Optional<WorkoutPlan> findByDayAndGoalType(DayOfWeek day, GoalType goalType);
    
    List<WorkoutPlan> findByGoalType(GoalType goalType);
}
