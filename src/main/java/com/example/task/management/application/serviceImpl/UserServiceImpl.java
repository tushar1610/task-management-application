package com.example.task.management.application.serviceImpl;

import com.example.task.management.application.entity.User;
import com.example.task.management.application.error.exception.ResourceNotFoundException;
import com.example.task.management.application.repository.UserRepository;
import com.example.task.management.application.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public User getDetails(String email){
        User user = userRepository.findByEmail(email);
        if(user == null){
            throw new ResourceNotFoundException("User not found with email : " + email);
        }
        return user;
    }

    @Override
    public User addUser(User user) {
        User newUser = User.builder()
                .email(user.getEmail())
                .password(passwordEncoder.encode(user.getPassword()))
                .role("USER")
                .build();

        return userRepository.save(newUser);
    }

}
