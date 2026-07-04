package com.shreyash.demo.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shreyash.demo.model.User;
import com.shreyash.demo.model.WorkoutPlan;

@Repository
public interface WorkoutPlanRepository extends JpaRepository<WorkoutPlan, Long> {
	List<WorkoutPlan> findByUserOrderByDay(User user);

    Optional<WorkoutPlan> findByIdAndUser(Long id, User user);

    void deleteByUser(User user);
}
