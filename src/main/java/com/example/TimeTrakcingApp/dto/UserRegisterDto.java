package com.example.TimeTrakcingApp.dto;

import com.example.TimeTrakcingApp.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterDto {

    @NotBlank
    @Size(min =2,max =50)
    @Pattern(regexp = "[a-zA-Z]+",message = "Моля,използвайте Латиница")
    private String username;

    @NotBlank
    @Size(min =3,max =50)
    @Pattern(regexp = "[а-яА-Я\\s\\d]+",message = "Моля,използвайте Кирилица")
    @Column(name = "employee_full_name")
    private String fullName;

    @NotBlank
    @Email()
    private String email;

    @NotBlank
    //@Size(min =4,max =20)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean enabled;
}
