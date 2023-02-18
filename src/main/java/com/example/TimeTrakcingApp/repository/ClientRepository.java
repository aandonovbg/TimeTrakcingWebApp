package com.example.TimeTrakcingApp.repository;

import com.example.TimeTrakcingApp.entity.Client;
import org.springframework.data.repository.CrudRepository;

public interface ClientRepository extends CrudRepository<Client,Long> {
}
