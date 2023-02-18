package com.example.TimeTrakcingApp.entity;

import com.example.TimeTrakcingApp.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
abstract public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotBlank
    @Size(min =2,max =50)
    @Pattern(regexp = "[a-zA-Z]+")
    private String username;
    @NotBlank
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role=Role.valueOf("EMPLOYEE");
    private boolean enabled;

    public void setPassword(String password) {
        BCryptPasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
        this.password = passwordEncoder.encode(password);
    }
}
