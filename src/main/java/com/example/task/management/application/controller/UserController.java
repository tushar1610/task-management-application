package com.example.task.management.application.controller;

import java.util.List;
import java.util.Objects;

import com.example.task.management.application.config.CustomUserDetails;
import com.example.task.management.application.config.JWTHelper;
import com.example.task.management.application.entity.User;
import com.example.task.management.application.error.exception.InternalServerException;
import com.example.task.management.application.error.exception.UserNotAuthorizedException;
import com.example.task.management.application.payload.JWTRequest;
import com.example.task.management.application.payload.JWTResponse;
import com.example.task.management.application.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTHelper jwtHelper;


    @CrossOrigin
    @PostMapping("/add")
    public ResponseEntity<User> addUser(@Valid @RequestBody User user){

        User savedUser = userService.addUser(user);
        if(savedUser == null){
            throw new InternalServerException("Could not create user. Try again later.");
        }
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }


    //    @CrossOrigin(origins = "http://localhost:3000",methods = RequestMethod.POST)
    @PostMapping("/login")
    public ResponseEntity<Object> loginUser(HttpServletRequest request, HttpServletResponse response, @RequestBody @Valid JWTRequest jwtRequest) throws UsernameNotFoundException{

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(), jwtRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String token = jwtHelper.generateToken(userDetails);


        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        JWTResponse jwtResponse = JWTResponse.builder()
                .jwtToken(token)
                .userId(userService.getDetails(userDetails.getUsername()).getUserId())
                .role(roles.get(0)).build();
        return new ResponseEntity<>(jwtResponse, HttpStatus.OK);

    }

    @CrossOrigin
    @GetMapping("/get/details")
    public ResponseEntity<User> getDetails(@RequestParam("email") String email){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null) throw new UserNotAuthorizedException("Access denied. Unauthorized access.");

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userService.getDetails(userDetails.getUsername());
        if(!Objects.equals(user.getEmail(), email)){
            throw new UserNotAuthorizedException("User email provided doesn't belong to logged in user");
        }
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }
}
