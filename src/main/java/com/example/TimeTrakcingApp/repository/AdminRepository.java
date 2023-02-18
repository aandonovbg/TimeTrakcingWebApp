package com.example.TimeTrakcingApp.repository;

import com.example.TimeTrakcingApp.entity.Admin;
import com.example.TimeTrakcingApp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin,Long> {
}
