package com.shreyash.demo.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "email_details")
public class Email {

	@Id
	@GeneratedValue(strategy =  GenerationType.IDENTITY)
	private Integer id;
	
	@Column(nullable = false)
	private String recipient;
	
	@Column(nullable = false)
	private String subject;
	
	@Column(nullable = false,length = 3000)
	private String message;
	
	@Column(nullable = false)
	private LocalDateTime sentAt;
	
	@Column(nullable = false)
	private String status;
}

