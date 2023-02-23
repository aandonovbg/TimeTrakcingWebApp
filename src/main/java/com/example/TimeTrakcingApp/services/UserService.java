package com.example.TimeTrakcingApp.services;

import com.example.TimeTrakcingApp.dto.UserRegisterDto;
import com.example.TimeTrakcingApp.entity.Admin;
import com.example.TimeTrakcingApp.entity.Employee;
import com.example.TimeTrakcingApp.entity.User;
import com.example.TimeTrakcingApp.enums.Role;
import com.example.TimeTrakcingApp.repository.EmployeeRepository;
import com.example.TimeTrakcingApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private EmployeeRepository employeeRepository;

    public boolean ifUserExists(String username){
        boolean exists = false;
        List<User> users = userRepository.findAll();
        for (User user : users) {
            if (user.getUsername().equals(username)){
                exists = true;
            }
        }
        return exists;
    }
    public boolean updateIfUserExists(User user){
        boolean exists = false;

        Optional<Employee> optionalEmployee = employeeRepository.findById(user.getId());
        String employeeName="";
        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();
            employeeName=employee.getUsername();
        }
        List<User> users = userRepository.getAllUsersExcludingUsername(employeeName);
        for (User account : users) {
            if (account.getUsername().equals(user.getUsername())){
                exists = true;
                break;
            }
        }

        return exists;
    }


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
            userRepository.save(admin);
        }else {
            employee.setEmail(registerDto.getEmail());
            employee.setPassword(registerDto.getPassword());
            employee.setFullName(registerDto.getFullName());
            employee.setUsername(registerDto.getUsername());
            employee.setRole(registerDto.getRole());
            employee.setEnabled(registerDto.isEnabled());
            userRepository.save(employee);
        }
        String to = registerDto.getEmail();
        String subject = "Потвърждение за създаден акаунт!";

        emailService.sendRegistrationEmail(to, subject, registerDto);
    }
    public void updateUser(User user, String password) throws MessagingException, IOException {
        userRepository.save(user);
        String to = user.getEmail();
        String subject = "Предупреждение за актуализиране на акаунт!";

        emailService.sendUpdateEmail(to, subject,user, password);
    }
}
