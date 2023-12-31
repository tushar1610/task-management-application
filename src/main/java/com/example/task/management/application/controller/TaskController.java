package com.example.task.management.application.controller;

import com.example.task.management.application.entity.Task;
import com.example.task.management.application.error.exception.InternalServerException;
import com.example.task.management.application.error.exception.ResourceNotFoundException;
import com.example.task.management.application.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping("/add")
    public ResponseEntity<Task> addTask(@Valid @RequestBody Task task){
        Task savedTask = taskService.addTask(task);
        if(savedTask == null){
            throw new InternalServerException("Could not create task. Try again later.");
        }
        return new ResponseEntity<>(savedTask, HttpStatus.CREATED);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable("id") Long id){
        Task task = taskService.getTaskById(id);
        if(task == null){
            throw new ResourceNotFoundException("No task with id : " + id);
        }
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Task> updateTaskById(@PathVariable("id") Long id, @RequestBody Task task){
        Task updatedTask = taskService.updateTaskById(id, task);
        if(updatedTask == null){
            throw new ResourceNotFoundException("No task with id : " + id);
        }
        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }

    @PutMapping("/complete/{id}")
    public ResponseEntity<Task> markTaskAsCompleted(@PathVariable("id") Long id){
        Task completedTask = taskService.markTaskAsCompleted(id);
        if(completedTask == null){
            throw new ResourceNotFoundException("No task with id : " + id);
        }
        return new ResponseEntity<>(completedTask, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Long> deleteTaskById(@PathVariable("id") Long id){
        if(taskService.getTaskById(id) == null){
            throw new ResourceNotFoundException("No task with id : " + id);
        }
        taskService.deleteTaskById(id);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }
}
