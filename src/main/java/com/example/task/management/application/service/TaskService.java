package com.example.task.management.application.service;

import com.example.task.management.application.entity.Task;

import java.util.List;

public interface TaskService {
    Task addTask(Long userId, Task task);

    Task getTaskById(Long taskId);

    Task updateTaskById(Long taskId, Task task);

    void deleteTaskById(Long taskId);

    Task markTaskAsCompleted(Long taskId);

    List<Task> getAllTasksByUserId(Long userId);
}
