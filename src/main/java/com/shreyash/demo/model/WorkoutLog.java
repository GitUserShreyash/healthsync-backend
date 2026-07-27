package com.shreyash.demo.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.shreyash.demo.enums.WorkoutIntensity;
import com.shreyash.demo.enums.WorkoutType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="workout_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private WorkoutType workoutType;

    private LocalDate day;
    
    private Integer durationMinutes;

    @Enumerated(EnumType.STRING)
    private WorkoutIntensity intensity;

    @Min(0)
    private Integer caloriesBurned;

    private Boolean completed;

    @Column(length = 500)
    private String notes;

    private LocalDateTime loggedAt;
}