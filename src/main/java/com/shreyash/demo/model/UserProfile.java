package com.shreyash.demo.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.shreyash.demo.enums.ActivityLevel;
import com.shreyash.demo.enums.ExperienceLevel;
import com.shreyash.demo.enums.Gender;
import com.shreyash.demo.enums.GoalType;
import com.shreyash.demo.enums.WorkoutIntensity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfile {

    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    private Integer age;
    
    private String appName;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private Double heightCm;

    private Double weightKg;

    @Enumerated(EnumType.STRING)
    private ActivityLevel activityLevel;

    @Enumerated(EnumType.STRING)
    private GoalType goal;

    @Enumerated(EnumType.STRING)
    private ExperienceLevel experienceLevel;

    @Enumerated(EnumType.STRING)
    private WorkoutIntensity workoutIntensity;
    
    private String dietPreference;

    private String timezone;

    private Double bmi;
    
    private String bmiCategory;

    private Double bodyFat;
    
    private Double recommendedWaterIntakeL;

    private Integer hydrationStreakDays = 0;
    
    private Integer recommendedCaloryIntake;
    
    private Boolean profileCompleted = false;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}