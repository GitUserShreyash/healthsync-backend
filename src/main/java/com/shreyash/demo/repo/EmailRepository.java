package com.shreyash.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shreyash.demo.model.Email;

@Repository
public interface EmailRepository extends JpaRepository<Email, Integer>{

}
