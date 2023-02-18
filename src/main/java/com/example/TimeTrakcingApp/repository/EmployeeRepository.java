package com.example.TimeTrakcingApp.repository;

import com.example.TimeTrakcingApp.entity.Employee;
import com.example.TimeTrakcingApp.services.AuthenticationService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Long> {
    AuthenticationService a=new AuthenticationService();
}
