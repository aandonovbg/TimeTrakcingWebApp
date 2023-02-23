package com.example.TimeTrakcingApp.services;

import com.example.TimeTrakcingApp.dto.UserRegisterDto;
import com.example.TimeTrakcingApp.entity.Admin;
import com.example.TimeTrakcingApp.entity.Employee;
import com.example.TimeTrakcingApp.enums.Role;
import com.example.TimeTrakcingApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;

@Service
public class CreateUserService {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private EmailService emailService;


    public void createUser(UserRegisterDto registerDto) throws MessagingException, IOException {
        Admin admin = new Admin();
        Employee employee = new Employee();
        if (registerDto.getRole().equals(Role.ADMIN)){
            admin.setEmail(registerDto.getEmail());
            admin.setPassword(registerDto.getPassword());
            admin.setFullName(registerDto.getFullName());
            admin.setUsername(registerDto.getUsername());
            admin.setRole(registerDto.getRole());
            admin.setEnabled(registerDto.isEnabled());
            userRepo.save(admin);
        }else {
            employee.setEmail(registerDto.getEmail());
            employee.setPassword(registerDto.getPassword());
            employee.setFullName(registerDto.getFullName());
            employee.setUsername(registerDto.getUsername());
            employee.setRole(registerDto.getRole());
            employee.setEnabled(registerDto.isEnabled());
            userRepo.save(employee);
        }
        String to = registerDto.getEmail();
        String subject = "Потвърждение за създаден акаунт!";

//        String text = "Здравейте " + registerDto.getFullName() + ",<br><br>" +
//                "Моля, използвай посочените по-долу потребител и парола!<br><br>"+
//                "<b>Потребител:</b> "+registerDto.getUsername()+"<br>"+
//                "<b>Парола:</b> "+registerDto.getPassword();

        emailService.sendRegistrationEmail(to, subject, registerDto);
    }
}
