package com.example.TimeTrakcingApp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "employees")
public class Employee extends User {
    @Column(name = "employee_daily_protocol", nullable = true)
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true) //1 employee can have multiple daily protocols
    private List<DailyProtocol> dailyProtocol=new ArrayList<>();
}
