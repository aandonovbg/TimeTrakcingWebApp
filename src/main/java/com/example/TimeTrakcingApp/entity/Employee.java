package com.example.TimeTrakcingApp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "employees")
public class Employee extends User {
    @Column(name = "employee_daily_protocol", nullable = true)
    @OneToMany //1 employee can have multiple daily protocols
    private List<DailyProtocol> dailyProtocol;
    @NotBlank
    @Size(min =2,max =50)
//    @Pattern(regexp = "[а-яА-Я]+")
    @Pattern(regexp = "[a-zA-Z]+")
    @Column(name = "employee_full_name")
    private String fullName;
}
