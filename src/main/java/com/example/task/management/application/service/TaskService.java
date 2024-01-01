package com.example.task.management.application.service;

import com.example.task.management.application.entity.Task;

import java.util.List;

public interface TaskService {
    Task addTask(Long userId, Task task);

    Task getTaskById(Long id, Long aLong);

    Task updateTaskById(Long id, Long taskId, Task task);

    void deleteTaskById(Long id);

    Task markTaskAsCompleted(Long id);

    List<Task> getAllTasksByUserId(Long userId);
}
