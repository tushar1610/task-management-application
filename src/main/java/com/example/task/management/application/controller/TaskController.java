package com.example.task.management.application.controller;

import com.example.task.management.application.config.CustomUserDetails;
import com.example.task.management.application.entity.Task;
import com.example.task.management.application.entity.User;
import com.example.task.management.application.error.exception.InternalServerException;
import com.example.task.management.application.error.exception.ResourceNotFoundException;
import com.example.task.management.application.error.exception.UserNotAuthorizedException;
import com.example.task.management.application.service.TaskService;
import com.example.task.management.application.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/user/{userId}/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    private boolean verifyAuthentication(Long userId) throws UserNotAuthorizedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null) return false;
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userService.getDetails(userDetails.getUsername());
        if(!Objects.equals(user.getUserId(), userId)){
            throw new UserNotAuthorizedException("User Id provided doesn't belong to logged in user");
        }
        return true;
    }

    @CrossOrigin
    @PostMapping("/add")
    public ResponseEntity<Task> addTask(@PathVariable("userId") Long userId, @Valid @RequestBody Task task){
        if (!verifyAuthentication(userId)) throw new UserNotAuthorizedException("Access denied. Unauthorized access.");

        Task savedTask = taskService.addTask(userId, task);
        if(savedTask == null){
            throw new InternalServerException("Could not create task. Try again later.");
        }
        return new ResponseEntity<>(savedTask, HttpStatus.CREATED);
    }

    @CrossOrigin
    @GetMapping("/get/{taskId}")
    public ResponseEntity<Task> getTaskById(@PathVariable("userId") Long userId, @PathVariable("taskId") Long taskId){
        if (!verifyAuthentication(userId)) throw new UserNotAuthorizedException("Access denied. Unauthorized access.");

        Task task = taskService.getTaskById(userId, taskId);
        if(task == null){
            throw new ResourceNotFoundException("No task with id : " + taskId + " for this user.");
        }
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/get/all")
    public ResponseEntity<List<Task>> getAllTasksByUserId(@PathVariable("userId") Long userId){
        if (!verifyAuthentication(userId)) throw new UserNotAuthorizedException("Access denied. Unauthorized access.");

        List<Task> tasks = taskService.getAllTasksByUserId(userId);
        if(tasks.isEmpty()){
            throw new ResourceNotFoundException("No tasks for user : " + userId);
        }
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @CrossOrigin
    @PutMapping("/update/{taskId}")
    public ResponseEntity<Task> updateTaskById(@PathVariable("userId") Long userId, @PathVariable("taskId") Long taskId, @RequestBody Task task){
        if (!verifyAuthentication(userId)) throw new UserNotAuthorizedException("Access denied. Unauthorized access.");

        Task updatedTask = taskService.updateTaskById(userId, taskId, task);
        if(updatedTask == null){
            throw new ResourceNotFoundException("No task with id : " + taskId);
        }
        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }

    @CrossOrigin
    @PutMapping("/completed/{taskId}")
    public ResponseEntity<Task> markTaskAsCompleted(@PathVariable("userId") Long userId, @PathVariable("taskId") Long taskId){
        if (!verifyAuthentication(userId)) throw new UserNotAuthorizedException("Access denied. Unauthorized access.");

        Task completedTask = taskService.markTaskAsCompleted(taskId);
        if(completedTask == null){
            throw new ResourceNotFoundException("No task with id : " + taskId);
        }
        return new ResponseEntity<>(completedTask, HttpStatus.OK);
    }

    @CrossOrigin
    @DeleteMapping("/delete/{taskId}")
    public ResponseEntity<String> deleteTaskById(@PathVariable("userId") Long userId, @PathVariable("taskId") Long taskId){
        if (!verifyAuthentication(userId)) throw new UserNotAuthorizedException("Access denied. Unauthorized access.");

        if(taskService.getTaskById(userId, taskId) == null){
            throw new ResourceNotFoundException("No task with id : " + taskId);
        }
        taskService.deleteTaskById(taskId);
        return new ResponseEntity<>("Task Id : " + taskId + " deleted successfully.", HttpStatus.OK);
    }
}
