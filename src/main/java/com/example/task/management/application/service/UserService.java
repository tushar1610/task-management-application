package com.example.task.management.application.service;

import com.example.task.management.application.entity.User;
import org.springframework.stereotype.Service;


@Service
public interface UserService {

    User getDetails(String email);

    User addUser(User user);
}
