package com.shreyash.demo.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shreyash.demo.model.Food;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {
	List<Food> findByCategoryContainingIgnoreCase(String category);
	List<Food> findByFoodNameContainingIgnoreCase(String foodName);
}
