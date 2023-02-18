package com.example.TimeTrakcingApp.entity;

import com.example.TimeTrakcingApp.enums.Role;

import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "admins")
public class Admin extends User{

}
