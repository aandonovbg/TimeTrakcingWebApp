package com.example.TimeTrakcingApp.repository;


import com.example.TimeTrakcingApp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    public User getUserByUsername(String username);
}
