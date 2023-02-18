package com.example.TimeTrakcingApp.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;


@Data //adds getters and setters
@Entity
@Builder
@NoArgsConstructor //generates a constructor with no arguments
@AllArgsConstructor //generates a constructor that takes all fields from the class
@Table(name = "daily_protocol")
public class DailyProtocol {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private LocalDate protocolDate;

    @ManyToMany
    @JoinTable(
            name = "protocol_client",
            joinColumns = @JoinColumn(name = "protocol_id"),
            inverseJoinColumns = @JoinColumn(name = "client_id"))
    private List<Client> client;
    @ManyToOne
    @JoinColumn(name = "employee_id",referencedColumnName = "id")
    private Employee employee;
    @NotNull
    @Column(name = "time_worked")
    private int timeWorked;
    @NotBlank
    @Size(min = 2 , max =200)
    private String description;





    @PrePersist
    public void prePersist() {
        LocalDate now=LocalDate.now();
        protocolDate = now;
    }
}
