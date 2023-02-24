package com.example.TimeTrakcingApp.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data //adds getters and setters
@Entity
@Builder
@NoArgsConstructor //generates a constructor with no arguments
@AllArgsConstructor //generates a constructor that takes all fields from the class
@Table(name = "clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    @Size(min =2,max =50)
    @Pattern(regexp = "[a-zA-Zа-яА-Я\\s\\d.,!?()-_]+")
    private String clientName;

    @NotBlank
    @Size(min =2,max =200)
    @Pattern(regexp = "[a-zA-Zа-яА-Я\\s\\d.,!?()-_]+")
    private String projectName;

    @NotBlank
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    @Column(name = "expiration_date")
    private String expirationDate;

}
